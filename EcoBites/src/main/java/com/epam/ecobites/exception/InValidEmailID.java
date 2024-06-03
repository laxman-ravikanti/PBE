package com.epam.ecobites.exception;

public class InValidEmailID extends RuntimeException {
	private String message;

	public InValidEmailID(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return "Exception: " + message;
	}
}
