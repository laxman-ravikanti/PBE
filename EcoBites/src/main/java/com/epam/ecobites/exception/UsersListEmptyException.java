package com.epam.ecobites.exception;

public class UsersListEmptyException extends RuntimeException {

	private String message;

	public UsersListEmptyException(String message) {
		super(message);
		this.message=message;
	}

	public String getMessage() {
		return "Exception : " + message;
	}
}
