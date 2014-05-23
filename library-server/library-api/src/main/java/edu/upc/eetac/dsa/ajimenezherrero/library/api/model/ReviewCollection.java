package edu.upc.eetac.dsa.ajimenezherrero.library.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.ajimenezherrero.library.api.BookResource;
import edu.upc.eetac.dsa.ajimenezherrero.library.api.MediaType;
import edu.upc.eetac.dsa.ajimenezherrero.library.api.ReviewResource;


public class ReviewCollection {
	@InjectLinks({
		//@InjectLink(resource = ReviewResource.class, style = Style.ABSOLUTE, rel = "reviews", title = "Self Reviews", type = MediaType.LIBRARY_API_REVIEW_COLLECTION),
		@InjectLink(value = "/reviews?before={before}", style = Style.ABSOLUTE, rel = "previous", title = "Previous reviews", type = MediaType.LIBRARY_API_REVIEW_COLLECTION, bindings = { @Binding(name = "before", value = "${instance.oldestTimestamp}") }),
		@InjectLink(value = "/reviews?after={after}", style = Style.ABSOLUTE, rel = "current", title = "Newest reviews", type = MediaType.LIBRARY_API_REVIEW_COLLECTION, bindings = { @Binding(name = "after", value = "${instance.newestTimestamp}") }),
	})
	private List<Link> links;
	private List<Review> reviews;
	private long newestTimestamp;
	private long oldestTimestamp;

	public ReviewCollection() {
		super();
		reviews = new ArrayList<>();
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public long getNewestTimestamp() {
		return newestTimestamp;
	}

	public void setNewestTimestamp(long newestTimestamp) {
		this.newestTimestamp = newestTimestamp;
	}

	public long getOldestTimestamp() {
		return oldestTimestamp;
	}

	public void setOldestTimestamp(long oldestTimestamp) {
		this.oldestTimestamp = oldestTimestamp;
	}
	
	public void addReview(Review review) {
		reviews.add(review);		
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
}
