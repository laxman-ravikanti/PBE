package com.epam.ecobites.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentials extends Exception {
	
	private String message;

	public InvalidCredentials(HttpStatus badRequest, String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return "Exception: " + message;
	}

}
