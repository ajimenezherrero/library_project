package edu.upc.eetac.dsa.ajimenezherrero.library.api.model;

public class LibraryError {
	private int status;
	private String message;
	
	public LibraryError(){
		super();
	}
	
	public LibraryError(int status, String message){
		super();
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
