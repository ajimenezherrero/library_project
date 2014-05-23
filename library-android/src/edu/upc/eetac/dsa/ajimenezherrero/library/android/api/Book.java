package edu.upc.eetac.dsa.ajimenezherrero.library.android.api;
import java.util.HashMap;
import java.util.Map;

public class Book {
	
	private Map<String, Link> links = new HashMap<>();
	private String idbook;
	private String title;
	private String author;
	private String lenguage;
	private String edition;
	private String editorial;
	private long edition_date;
	private long print_date;
	
	public Map<String, Link> getLinks() {
		return links;
	}
	
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
	public String getEdition() {
		return edition;
	}
	public void setEdition(String edition) {
		this.edition = edition;
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
	
}