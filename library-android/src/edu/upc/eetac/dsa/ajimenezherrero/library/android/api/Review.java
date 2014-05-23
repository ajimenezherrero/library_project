package edu.upc.eetac.dsa.ajimenezherrero.library.android.api;

import java.util.HashMap;
import java.util.Map;

public class Review {
	private Map<String, Link> links = new HashMap<>();
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
	
	public Map<String, Link> getLinks() {
		return links;
	}
	
}