package edu.upc.eetac.dsa.ajimenezherrero.library.android.api;

import java.util.HashMap;
import java.util.Map;

public class LibraryRootAPI {

	private Map<String, Link> links;
	 
	public LibraryRootAPI() {
		links = new HashMap<>();
	}
 
	public Map<String, Link> getLinks() {
		return links;
	}
	
}