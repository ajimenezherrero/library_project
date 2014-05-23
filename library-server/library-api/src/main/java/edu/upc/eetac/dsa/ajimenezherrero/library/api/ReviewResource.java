package edu.upc.eetac.dsa.ajimenezherrero.library.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import edu.upc.eetac.dsa.ajimenezherrero.library.api.model.Review;
import edu.upc.eetac.dsa.ajimenezherrero.library.api.model.ReviewCollection;

@Path("/books/{idbook}/reviews")
public class ReviewResource {

	@Context
	private SecurityContext security;
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	
	@GET
	@Produces(MediaType.LIBRARY_API_REVIEW_COLLECTION)
	public ReviewCollection getReviews(@PathParam("idbook") String idbook,
			@Context Request request, @QueryParam("length") int length,
			@QueryParam("before") long before, @QueryParam("after") long after) {
		ReviewCollection reviews = new ReviewCollection();
	 
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	 
		PreparedStatement stmt = null;
		try {
			boolean updateFromLast = after > 0;
			stmt = conn.prepareStatement(buildGetReviewsQuery(updateFromLast));
			stmt.setInt(1, Integer.valueOf(idbook));
			if (updateFromLast) {
				stmt.setTimestamp(2, new Timestamp(after));
			} else {
				if (before > 0)
					stmt.setTimestamp(2, new Timestamp(before));
				else
					stmt.setTimestamp(2, null);
				length = (length <= 0) ? 5 : length;
				stmt.setInt(3, length);
			}
			ResultSet rs = stmt.executeQuery();
			boolean first = true;
			long oldestTimestamp = 0;
			while (rs.next()) {
				Review review = new Review();
				review.setIdreview(rs.getString("idreview"));
				review.setUsername(rs.getString("username"));
				review.setIdbook(rs.getString("idbook"));
				review.setText(rs.getString("text"));
				review.setDate_review(rs.getLong("date_review"));
				if (first) {
					first = false;
					reviews.setNewestTimestamp(review.getDate_review());
				}
				reviews.addReview(review);
			}
			reviews.setOldestTimestamp(oldestTimestamp);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
	 
		return reviews;
	}
	
	private String buildGetReviewsQuery(boolean updateFromLast) {
		if (updateFromLast)
			return "select r.* from reviews r where r.idbook = ? and r.date_review > ? order by date_review desc";
		else
			return "select r.* from reviews r where r.idbook = ? and r.date_review < ifnull(?, now())  order by date_review desc limit ?";
	}
	
	@GET
	@Path("/{idreview}")
	@Produces(MediaType.LIBRARY_API_REVIEW)
	public Review getReview(@PathParam("idbook") String idbook,@PathParam("idreview") String idreview,
			@Context Request request) {
		Review review = new Review();
	 
		review = getReviewFromDatabase(idreview, idbook);
	 
		return review;
	}
	
	private String buildGetReviewQuery() {
		return "select * from reviews where  idreview = ? and idbook = ?";
	}
	
	@POST
	@Consumes(MediaType.LIBRARY_API_REVIEW)
	@Produces(MediaType.LIBRARY_API_REVIEW)
	public Review createReview(@PathParam("idbook") String idbook, Review review) {
		validateRoleRegistered();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildInsertReview();
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, security.getUserPrincipal().getName());
			stmt.setInt(2, Integer.valueOf(idbook));
			stmt.setString(3, review.getText());
			
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int idreview = rs.getInt(1);
				review = getReviewFromDatabase(Integer.toString(idreview), idbook);
			} else {
				// Something has failed...
			}
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return review;
	}
	
	private String buildInsertReview() {
		return "insert into reviews (username, idbook, text) value (?, ?, ?)";
	}
	
	@DELETE
	@Path("/{idreview}")
	public void deleteSting(@PathParam("idreview") String idreview,@PathParam("idbook") String idbook) {
		validateUserDelete(idreview,idbook);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildDeleteReviewByBookId();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, Integer.valueOf(idreview));
			int rows = stmt.executeUpdate();
			if (rows == 0)
				throw new NotFoundException("There's no review with idreview="
						+ idreview);
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
	}
	
	private String buildDeleteReviewByBookId(){
		return "delete from reviews where idreview = ?";
	}
	
	private void validateUserDelete(String idreview, String idbook) {
		Review currentReview = getReviewFromDatabase(idreview, idbook);
		if ((!security.getUserPrincipal().getName().equals(currentReview.getUsername())) && (!security.isUserInRole("administrator")))
			throw new ForbiddenException("You are not allowed to modify this sting.");
	}
	
	
	
	@PUT
	@Path("/{idreview}")
	@Consumes(MediaType.LIBRARY_API_REVIEW)
	@Produces(MediaType.LIBRARY_API_REVIEW)
	public Review updateBook(@PathParam("idbook") String idbook,@PathParam("idreview") String idreview, Review review) {
		validateUpdateReview(review);
		validateUserPut(idreview,idbook);
		
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildUpdateReview();
			stmt = conn.prepareStatement(sql);

			stmt.setString(1, review.getText());
			stmt.setInt(2, Integer.valueOf(idreview));
			
			int rows = stmt.executeUpdate();
			if (rows == 1)
				review = getReviewFromDatabase(idreview, idbook);
			else {
				throw new NotFoundException("There's no book with idbook="
						+ idbook);
			}

		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return review;
	}
	
	private String buildUpdateReview() {
		return "update reviews set text=ifnull(?, text) where idreview = ? ";
	}
	
	private void validateUpdateReview(Review review) {
		if (review.getText() != null & review.getText().length() > 500)
			throw new BadRequestException(
					"Text can't be greater than 500 characters.");
	}
	
	private void validateRoleRegistered() {
		if (!security.isUserInRole("registered"))
			throw new ForbiddenException(
					"You are not allowed to modify this sting.");
	}
	
	private void validateUserPut(String idreview, String idbook) {
		Review currentReview = getReviewFromDatabase(idreview, idbook);
		if (!security.getUserPrincipal().getName().equals(currentReview.getUsername()))
			throw new ForbiddenException(
					"You are not allowed to modify this sting.");
	}
	
	
	private Review getReviewFromDatabase(String idreview, String idbook) {
		Review review = new Review();
		 
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	 
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(buildGetReviewQuery());
			stmt.setInt(1, Integer.valueOf(idreview));
			stmt.setInt(2, Integer.valueOf(idbook));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				review.setIdreview(rs.getString("idreview"));
				review.setUsername(rs.getString("username"));
				review.setIdbook(rs.getString("idbook"));
				review.setText(rs.getString("text"));
				review.setDate_review(rs.getLong("date_review"));
			} else {
				throw new NotFoundException("There's no review with idreview="
						+ idreview);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
	 
		return review;
	}
}
