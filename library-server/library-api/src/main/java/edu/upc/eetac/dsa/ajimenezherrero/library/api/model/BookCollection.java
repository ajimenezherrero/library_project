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

public class BookCollection {
	@InjectLinks({
		@InjectLink(resource = BookResource.class, style = Style.ABSOLUTE, rel = "self books", title = "Self Book", type = MediaType.LIBRARY_API_BOOK_COLLECTION),
		@InjectLink(resource = BookResource.class, style = Style.ABSOLUTE, rel = "create-book", title = "Create book", type = MediaType.LIBRARY_API_BOOK),
		@InjectLink(value = "/books?before={before}", style = Style.ABSOLUTE, rel = "previous", title = "Previous books", type = MediaType.LIBRARY_API_BOOK_COLLECTION, bindings = { @Binding(name = "before", value = "${instance.oldestTimestamp}") }),
		@InjectLink(value = "/books?after={after}", style = Style.ABSOLUTE, rel = "current", title = "Newest books", type = MediaType.LIBRARY_API_BOOK_COLLECTION, bindings = { @Binding(name = "after", value = "${instance.newestTimestamp}") }),
		@InjectLink(value = "/books?search=", style = Style.ABSOLUTE, rel = "search", title = "Search books", type = MediaType.LIBRARY_API_BOOK_COLLECTION),
	})
	private List<Link> links;
	private List<Book> books;
	private long newestTimestamp;
	private long oldestTimestamp;
	private String search;
	
	
	public BookCollection() {
		super();
		books = new ArrayList<>();
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
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

	public void addBook(Book book) {
		books.add(book);		
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}
	
	
}
