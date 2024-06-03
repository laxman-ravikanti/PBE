package com.epam.ecobites;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.epam.ecobites.controller.UserController;
import com.epam.ecobites.model.LoginDTO;
import com.epam.ecobites.service.JwtServiceImpl;
import com.epam.ecobites.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class ControllerTestCases {

	private MockMvc mockMvc;

	@Mock
	private UserService userService;

	@Mock
	private JwtServiceImpl jwtServiceImpl;

	@Mock
	private AuthenticationManager authenticationManager;

	@InjectMocks
	private UserController userController;

	ObjectMapper objectMapper = new ObjectMapper();
	com.fasterxml.jackson.databind.ObjectWriter objectWriter = objectMapper.writer();

	@BeforeEach
	void setUp() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}

	@Test
	public void testValidAuthentication() {
		when(userService.checkLoginDetails(any(LoginDTO.class))).thenReturn(true);

		Authentication authentication = mock(Authentication.class);
		when(authentication.isAuthenticated()).thenReturn(true);
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authentication);

		when(jwtServiceImpl.generateToken(any(String.class))).thenReturn("mocked_token");

		LoginDTO authRequest = new LoginDTO();
		authRequest.setEmailId("test@example.com");
		authRequest.setPassword("password");

		ResponseEntity<String> responseEntity = userController.authenticateAndGetToken(authRequest);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("mocked_token", responseEntity.getBody());
	}

	@Test
	public void testInvalidAuthentication() {
		when(userService.checkLoginDetails(any(LoginDTO.class))).thenReturn(false);

		LoginDTO authRequest = new LoginDTO();
		authRequest.setEmailId("test@example.com");
		authRequest.setPassword("password");

		ResponseEntity<String> responseEntity = userController.authenticateAndGetToken(authRequest);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid credentials. Unable to authenticate user.", responseEntity.getBody());
	}

	@Test
	public void testAuthenticationFailure() {
		when(userService.checkLoginDetails(any(LoginDTO.class))).thenReturn(true);

		Authentication authentication = mock(Authentication.class);
		when(authentication.isAuthenticated()).thenReturn(false);
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authentication);

		LoginDTO authRequest = new LoginDTO();
		authRequest.setEmailId("test@example.com");
		authRequest.setPassword("password");

		ResponseEntity<String> responseEntity = userController.authenticateAndGetToken(authRequest);

		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertEquals("Invalid credentials. Unable to authenticate user.", responseEntity.getBody());
	}

	@Test
	public void authenticateAndGetToken_invalidEmail_returnsBadRequest() throws Exception {
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setEmailId("non-existent@email.com");
		loginDTO.setPassword("password");

		when(userService.checkLoginDetails(any(LoginDTO.class))).thenReturn(false);

		mockMvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(loginDTO))).andExpect(status().isBadRequest());
	}

	@Test
	public void authenticateAndGetToken_invalidPassword_returnsBadRequest() throws Exception {
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setEmailId("test@email.com");
		loginDTO.setPassword("wrong_password");

		when(userService.checkLoginDetails(any(LoginDTO.class))).thenReturn(false);

		mockMvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(loginDTO))).andExpect(status().isBadRequest());
	}

}
