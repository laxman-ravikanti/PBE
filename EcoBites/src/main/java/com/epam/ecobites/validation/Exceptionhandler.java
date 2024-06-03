package com.epam.ecobites.validation;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.epam.ecobites.exception.InValidEmailID;
import com.epam.ecobites.exception.InValidPassword;
import com.epam.ecobites.exception.InvalidCredentials;
import com.epam.ecobites.exception.ValidEmailID;

@RestControllerAdvice
public class Exceptionhandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ExceptionResponse handleMethodArgument(MethodArgumentNotValidException e, WebRequest webRequest) {
		List<String> details = new ArrayList<>();
		e.getBindingResult().getFieldErrors().forEach(fieldError -> {
			details.add(fieldError.getDefaultMessage());
		});

		return new ExceptionResponse(HttpStatus.BAD_REQUEST.name(), details.toString(),
				webRequest.getDescription(false));
	}

	@ExceptionHandler(InvalidCredentials.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ExceptionResponse invalidCredsException(InvalidCredentials e, WebRequest webRequest) {

		String expception = e.getMessage();

		return new ExceptionResponse(HttpStatus.BAD_REQUEST.name(), expception, webRequest.getDescription(false));

	}

	@ExceptionHandler(InValidEmailID.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ExceptionResponse invalidCredsException(InValidEmailID e, WebRequest webRequest) {

		String expception = e.getMessage();

		return new ExceptionResponse(HttpStatus.BAD_REQUEST.name(), expception, webRequest.getDescription(false));
	}

	@ExceptionHandler(InValidPassword.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ExceptionResponse invalidCredsException(InValidPassword e, WebRequest webRequest) {

		String expception = e.getMessage();

		return new ExceptionResponse(HttpStatus.BAD_REQUEST.name(), expception, webRequest.getDescription(false));

	}

	@ExceptionHandler(ValidEmailID.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ExceptionResponse invalidCredsException(ValidEmailID e, WebRequest webRequest) {

		String expception = e.getMessage();

		return new ExceptionResponse(HttpStatus.BAD_REQUEST.name(), expception, webRequest.getDescription(false));
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public ExceptionResponse invalidCredsException(AccessDeniedException e, WebRequest webRequest) {

		return new ExceptionResponse(HttpStatus.UNAUTHORIZED.name(), "you are not autherized for this functioncality",
				webRequest.getDescription(false));
	}

}
