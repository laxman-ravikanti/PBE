package com.epam.ecobites.exception;

public class ValidEmailID extends RuntimeException{
	private String message;

	public ValidEmailID(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return "Exception: " + message;
	}
}
