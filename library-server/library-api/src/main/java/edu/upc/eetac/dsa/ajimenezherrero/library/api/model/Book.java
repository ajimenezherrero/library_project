package edu.upc.eetac.dsa.ajimenezherrero.library.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.ajimenezherrero.library.api.BookResource;
import edu.upc.eetac.dsa.ajimenezherrero.library.api.MediaType;
import edu.upc.eetac.dsa.ajimenezherrero.library.api.ReviewResource;



public class Book {
	@InjectLinks({
		@InjectLink(resource = ReviewResource.class, style = Style.ABSOLUTE, rel = "bookreviews", title = "Latest reviews", type = MediaType.LIBRARY_API_REVIEW_COLLECTION),
		@InjectLink(resource = BookResource.class, style = Style.ABSOLUTE, rel = "books", title = "Latest books", type = MediaType.LIBRARY_API_BOOK_COLLECTION),
		@InjectLink(resource = ReviewResource.class, style = Style.ABSOLUTE, rel = "create-review", title = "Create review", type = MediaType.LIBRARY_API_REVIEW),
		@InjectLink(resource = BookResource.class, style = Style.ABSOLUTE, rel = "self book", title = "Book", type = MediaType.LIBRARY_API_BOOK, method = "getBook", bindings = @Binding(name = "idbook", value = "${instance.idbook}")),
	})
	private List<Link> links;
	private String idbook;
	private String title;
	private String author;
	private String lenguage;
	private String edition;
	private String editorial;
	private long edition_date;
	private long print_date;
	
	
	public String getIdbook() {
		return idbook;
	}
	public void setIdbook(String idbook) {
		this.idbook = idbook;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getLenguage() {
		return lenguage;
	}
	public void setLenguage(String lenguage) {
		this.lenguage = lenguage;
	}
	public String getEditorial() {
		return editorial;
	}
	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}
	public long getEdition_date() {
		return edition_date;
	}
	public void setEdition_date(long edition_date) {
		this.edition_date = edition_date;
	}
	public long getPrint_date() {
		return print_date;
	}
	public void setPrint_date(long print_date) {
		this.print_date = print_date;
	}
	public String getEdition() {
		return edition;
	}
	public void setEdition(String edition) {
		this.edition = edition;
	}
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
}
