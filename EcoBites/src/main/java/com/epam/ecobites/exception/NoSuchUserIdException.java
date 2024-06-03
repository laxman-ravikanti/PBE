package com.epam.ecobites.exception;

public class NoSuchUserIdException extends RuntimeException{
	private String message;

	public NoSuchUserIdException(String message) {
		super(message);
		this.message=message;
	}

	public String getMessage() {
		return "Exception : " + message;
	}
}
