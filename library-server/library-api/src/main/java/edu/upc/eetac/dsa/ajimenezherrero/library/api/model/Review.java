package edu.upc.eetac.dsa.ajimenezherrero.library.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.ajimenezherrero.library.api.MediaType;
import edu.upc.eetac.dsa.ajimenezherrero.library.api.ReviewResource;

public class Review {
	@InjectLinks({
		@InjectLink(resource = ReviewResource.class, style = Style.ABSOLUTE, rel = "self-review", title = "Edit review", type = MediaType.LIBRARY_API_REVIEW, method = "getReview", bindings = @Binding(name = "idreview", value = "${instance.idreview}")),
		@InjectLink(resource = ReviewResource.class, style = Style.ABSOLUTE, rel = "reviews", title = "Reviews", type = MediaType.LIBRARY_API_REVIEW),
	})
	private List<Link> links;
	private String idreview;
	private String username;
	private String idbook;
	private String text;
	private long date_review;
	public String getIdreview() {
		return idreview;
	}
	public void setIdreview(String idreview) {
		this.idreview = idreview;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getIdbook() {
		return idbook;
	}
	public void setIdbook(String idbook) {
		this.idbook = idbook;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public long getDate_review() {
		return date_review;
	}
	public void setDate_review(long date_review) {
		this.date_review = date_review;
	}
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	
}
