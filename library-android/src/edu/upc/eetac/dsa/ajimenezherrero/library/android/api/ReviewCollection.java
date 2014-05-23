package edu.upc.eetac.dsa.ajimenezherrero.library.android.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewCollection {
	private Map<String, Link> links = new HashMap<>();
	private List<Review> reviews = new ArrayList<>();;
	private long newestTimestamp;
	private long oldestTimestamp;
	
	
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
	public List<Review> getReviews() {
		return reviews;
	}
	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	
}