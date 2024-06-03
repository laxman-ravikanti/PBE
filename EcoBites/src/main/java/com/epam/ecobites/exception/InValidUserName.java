package com.epam.ecobites.exception;

public class InValidUserName extends RuntimeException {
	
	private String message;
	
	public InValidUserName(String message) {
		super(message);
		this.message=message;
	}
	
	public String getMessage() {
		return "Exception : "+ message;
	}

}
