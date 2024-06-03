package com.epam.ecobites.exception;

public class UserExistException extends RuntimeException {

	public UserExistException(String message) {
		super(message);
	}

}
