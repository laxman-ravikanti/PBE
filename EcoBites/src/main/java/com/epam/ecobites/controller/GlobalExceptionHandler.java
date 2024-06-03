package com.epam.ecobites.controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.epam.ecobites.exception.ExceptionResponse;
import com.epam.ecobites.exception.UserExistException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserExistException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public final ExceptionResponse handleUserAlreadyExistException(UserExistException exception, WebRequest request) {
		return new ExceptionResponse(new Date().toString(), exception.getMessage(), request.getDescription(false));
	}

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ExceptionResponse inputNotValidException(MethodArgumentNotValidException exception, WebRequest request) {
		Map<String, String> errors = new LinkedHashMap<>();
		exception.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return new ExceptionResponse(new Date().toString(), errors.toString(), request.getDescription(false));
	}
}
