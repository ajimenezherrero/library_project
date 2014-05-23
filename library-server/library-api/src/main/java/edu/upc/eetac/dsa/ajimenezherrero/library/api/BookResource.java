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

import edu.upc.eetac.dsa.ajimenezherrero.library.api.model.Book;
import edu.upc.eetac.dsa.ajimenezherrero.library.api.model.BookCollection;

@Path("/books")
public class BookResource {
	@Context
	private SecurityContext security;
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	@GET
	@Produces(MediaType.LIBRARY_API_BOOK_COLLECTION)
	public BookCollection getBooks(@QueryParam("length") int length,
			@QueryParam("before") long before, @QueryParam("after") long after,
			@QueryParam("search") String search) {
		BookCollection books = new BookCollection();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		PreparedStatement stmt = null;
		try {
			boolean updateFromLast = after > 0;
			boolean updateSearch = search != null;
			stmt = conn.prepareStatement(buildGetBooksQuery(updateFromLast,
					updateSearch));
			if (updateSearch){ 
				stmt.setString(1, "%" + search + "%");
				stmt.setString(2, "%" + search + "%");
			}
			else if (updateFromLast) {
				stmt.setTimestamp(1, new Timestamp(after));
			} else {
				if (before > 0)
					stmt.setTimestamp(1, new Timestamp(before));
				else
					stmt.setTimestamp(1, null);
				length = (length <= 0) ? 20 : length;
				stmt.setInt(2, length);
			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Book book = new Book();
				book.setIdbook(rs.getString("idbook"));
				book.setTitle(rs.getString("title"));
				book.setAuthor(rs.getString("author"));
				book.setLenguage(rs.getString("lenguage"));
				book.setEdition_date(rs.getTimestamp("edition_date").getTime());
				book.setPrint_date(rs.getTimestamp("print_date").getTime());
				book.setEditorial(rs.getString("editorial"));
				books.addBook(book);
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

		return books;
	}

	private String buildGetBooksQuery(boolean updateFromLast,
			boolean updateSearch) {
		if (updateSearch)
			return "select * from book where author like ? or title like ? order by edition_date desc";
		else if (updateFromLast)
			return "select * from book where edition_date > ? order by edition_date desc";
		else
			return "select * from book where edition_date < ifnull(?, now())  order by edition_date desc limit ?";

	}

	@GET
	@Path("/{idbook}")
	@Produces(MediaType.LIBRARY_API_BOOK)
	public Book getBook(@PathParam("idbook") String idbook,
			@Context Request request) {
		Book book = new Book();
		book = getBookFromDatabase(idbook);
		return book;
	}

	private String buildGetBookByIdQuery() {
		return "select * from book where idbook = ?";
	}

	@POST
	@Consumes(MediaType.LIBRARY_API_BOOK)
	@Produces(MediaType.LIBRARY_API_BOOK)
	public Book createBook(Book book) {
		validateRoleAdministrator();
		validateBook(book);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildInsertBook();
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, book.getTitle());
			stmt.setString(2, book.getAuthor());
			stmt.setString(3, book.getLenguage());
			stmt.setString(4, book.getEdition());
			stmt.setTimestamp(5, new Timestamp(book.getEdition_date()));
			stmt.setTimestamp(6, new Timestamp(book.getPrint_date()));
			stmt.setString(7, book.getEditorial());

			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int idbook = rs.getInt(1);
				book = getBookFromDatabase(Integer.toString(idbook));
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

		return book;
	}

	private void validateBook(Book book) {
		if (book.getTitle() == null)
			throw new BadRequestException("Subject can't be null.");
		if (book.getAuthor() == null)
			throw new BadRequestException("Content can't be null.");
		if (book.getTitle().length() > 255)
			throw new BadRequestException(
					"Title can't be greater than 255 characters.");
		if (book.getAuthor().length() > 70)
			throw new BadRequestException(
					"Author can't be greater than 70 characters.");
	}

	private String buildInsertBook() {
		return "insert into book (title, author, lenguage, edition, edition_date, print_date, editorial) value (?, ?, ?, ?, ?, ?, ?)";
	}

	private Book getBookFromDatabase(String idbook) {
		Book book = new Book();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(buildGetBookByIdQuery());
			stmt.setInt(1, Integer.valueOf(idbook));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				book.setIdbook(rs.getString("idbook"));
				book.setTitle(rs.getString("title"));
				book.setAuthor(rs.getString("author"));
				book.setLenguage(rs.getString("lenguage"));
				book.setEdition_date(rs.getTimestamp("edition_date").getTime());
				book.setPrint_date(rs.getTimestamp("print_date").getTime());
				book.setEditorial(rs.getString("editorial"));

			} else {
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

		return book;
	}

	@PUT
	@Path("/{idbook}")
	@Consumes(MediaType.LIBRARY_API_BOOK)
	@Produces(MediaType.LIBRARY_API_BOOK)
	public Book updateBook(@PathParam("idbook") String idbook, Book book) {
		validateRoleAdministrator();
		validateUpdateBook(book);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildUpdateBook();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, book.getTitle());
			stmt.setString(2, book.getAuthor());
			stmt.setString(3, book.getLenguage());
			stmt.setString(4, book.getEdition());
			stmt.setString(5, book.getEditorial());
			stmt.setInt(6, Integer.valueOf(idbook));

			int rows = stmt.executeUpdate();
			if (rows == 1)
				book = getBookFromDatabase(idbook);
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

		return book;
	}

	private void validateUpdateBook(Book book) {
		if (book.getTitle() != null & book.getTitle().length() > 255)
			throw new BadRequestException(
					"Subject can't be greater than 255 characters.");
		if (book.getAuthor() != null & book.getAuthor().length() > 70)
			throw new BadRequestException(
					"Content can't be greater than 70 characters.");
	}

	private String buildUpdateBook() {
		return "update book set title=ifnull(?, title),author=ifnull(?, author),lenguage=ifnull(?, lenguage),edition=ifnull(?, edition), editorial=ifnull(?, editorial) where idbook=?";
	}

	@DELETE
	@Path("/{idbook}")
	public void deleteBook(@PathParam("idbook") String idbook) {
		validateRoleAdministrator();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		try {
			String sql = buildDeleteBook();
			String sql2 = buildDeleteReviewByBookId();
			stmt = conn.prepareStatement(sql);
			stmt2 = conn.prepareStatement(sql2);
			stmt.setInt(1, Integer.valueOf(idbook));
			stmt2.setInt(1, Integer.valueOf(idbook));
			stmt2.executeUpdate();
			int rows = stmt.executeUpdate();

			if (rows == 0)
				throw new NotFoundException("There's no book with idbook="
						+ idbook);
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

	private String buildDeleteBook() {
		return "delete from book where idbook=?";
	}

	private String buildDeleteReviewByBookId() {
		return "delete from reviews where idbook = ?";
	}

	private void validateRoleAdministrator() {
		if (!security.isUserInRole("administrator"))
			throw new ForbiddenException(
					"You are not allowed to modify this sting.");
	}
}
