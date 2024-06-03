package com.epam.ecobites.exception;



import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerControllerTest {

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private InvalidTokenException invalidTokenException;

    @Mock
    private WebRequest webRequest;

    @Mock
    private ObjectError objectError;

    @InjectMocks
    private ExceptionHandlerController exceptionHandlerController;

    @Test
    void testInvalidInputException() {
        when(objectError.getDefaultMessage()).thenReturn("Invalid input");
        when(methodArgumentNotValidException.getAllErrors()).thenReturn(Arrays.asList(objectError, objectError));
        when(webRequest.getDescription(false)).thenReturn("Entered input is invalid");

        ResponseEntity<ExceptionResponse> response = exceptionHandlerController.invalidInputException(methodArgumentNotValidException, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("[Invalid input, Invalid input]", response.getBody().getError());
    }

    @Test
    void testHandleUserNotFoundException() {
        when(invalidTokenException.getMessage()).thenReturn("user not found");

        ResponseEntity<?> response = exceptionHandlerController.handleInvalidTokenException(invalidTokenException, webRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("user not found", ((ExceptionResponse) response.getBody()).getError());
    }
}


