package com.epam.ecobites.exception;

public class InValidPassword extends RuntimeException {
	private String message;

	public InValidPassword(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return "Exception: " + message;
	}

}
