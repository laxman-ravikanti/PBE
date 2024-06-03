package com.epam.ecobites.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExceptionHandlerController {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> invalidInputException(MethodArgumentNotValidException exception,WebRequest request){
		List<String> errors = new ArrayList<>();
		exception.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()) );
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(), errors.toString(),request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse,HttpStatus.BAD_REQUEST);
	}


	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<Object> handleInvalidTokenException(InvalidTokenException ex, WebRequest request) {
		ExceptionResponse responseDetails = new ExceptionResponse(new Date().toString(), ex.getMessage(), request.getDescription(false));
		logger.error("An error occurred: ", ex);
		return new ResponseEntity<>(responseDetails, HttpStatus.UNAUTHORIZED);
	}






}

