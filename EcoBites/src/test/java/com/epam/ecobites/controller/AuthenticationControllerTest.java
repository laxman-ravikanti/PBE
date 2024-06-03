package com.epam.ecobites.controller;

import com.epam.ecobites.exception.AuthenticationResponse;
import com.epam.ecobites.exception.InvalidTokenException;
import com.epam.ecobites.model.User;
import com.epam.ecobites.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

	@InjectMocks
	private AuthenticationController authenticationController;

	@Mock
	private AuthenticationService authenticationService;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}


	@Test
	void testLogin() {
		User user = new User();
		AuthenticationResponse authResponse = new AuthenticationResponse("token", "username");
		when(authenticationService.authenticate(user)).thenReturn(authResponse);

		ResponseEntity<AuthenticationResponse> responseEntity = authenticationController.login(user);

		assertEquals(ResponseEntity.ok(authResponse), responseEntity);
		verify(authenticationService, times(1)).authenticate(user);
	}


	@Test
	void testLogout() {
		Authentication authentication = mock(Authentication.class);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		doNothing().when(authenticationService).logout(request, response, authentication);

		ResponseEntity<?> responseEntity = authenticationController.logout(request, response);

		assertEquals(ResponseEntity.ok("Logout successful"), responseEntity);
		verify(authenticationService, times(1)).logout(request, response, authentication);
	}


	@Test
	void testHandleUserNotFoundError() {
		assertThrows(InvalidTokenException.class, () -> authenticationController.handleUserNotFoundError());
	}
}
