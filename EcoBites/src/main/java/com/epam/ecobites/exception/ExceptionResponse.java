package com.epam.ecobites.exception;

public class ExceptionResponse {

	String timeStamp;
	String error;
	String path;

	public ExceptionResponse() {
	}

	public ExceptionResponse(String timeStamp, String error, String path) {
		super();
		this.timeStamp = timeStamp;
		this.error = error;
		this.path = path;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
