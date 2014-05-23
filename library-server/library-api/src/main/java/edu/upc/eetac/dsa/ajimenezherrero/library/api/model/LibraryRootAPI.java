package edu.upc.eetac.dsa.ajimenezherrero.library.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.ajimenezherrero.library.api.BookResource;
import edu.upc.eetac.dsa.ajimenezherrero.library.api.LibraryRootAPIResource;
import edu.upc.eetac.dsa.ajimenezherrero.library.api.MediaType;



public class LibraryRootAPI {
	@InjectLinks({
		@InjectLink(resource = LibraryRootAPIResource.class, style = Style.ABSOLUTE, rel = "self bookmark home", title = "Library Root API", method = "getRootAPI"),
		@InjectLink(resource = BookResource.class, style = Style.ABSOLUTE, rel = "books", title = "Latest books", type = MediaType.LIBRARY_API_BOOK_COLLECTION),
		@InjectLink(resource = BookResource.class, style = Style.ABSOLUTE, rel = "create-books", title = "Latest books", type = MediaType.LIBRARY_API_BOOK) })
	private List<Link> links;
 
	public List<Link> getLinks() {
		return links;
	}
 
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
}
