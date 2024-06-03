package com.epam.ecobites.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
@NoArgsConstructor
public class ExceptionResponse {

	private String timeStamp;
	private String error;
	private String path;
}
