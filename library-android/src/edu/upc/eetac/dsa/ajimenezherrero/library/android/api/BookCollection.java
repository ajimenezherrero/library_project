package edu.upc.eetac.dsa.ajimenezherrero.library.android.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class BookCollection {

	private Map<String, Link> links = new HashMap<>();
	private List<Book> books = new ArrayList<>();;
	private long newestTimestamp;
	private long oldestTimestamp;
	
	public List<Book> getBooks() {
		return books;
	}
	
	public void addBook(Book book) {
		books.add(book);
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
	
	public Map<String, Link> getLinks() {
		return links;
	}
	
}
