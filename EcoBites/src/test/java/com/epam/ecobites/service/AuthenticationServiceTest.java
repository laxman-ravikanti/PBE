package com.epam.ecobites.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.epam.ecobites.exception.AuthenticationResponse;
import com.epam.ecobites.exception.InvalidTokenException;
import com.epam.ecobites.model.Token;
import com.epam.ecobites.model.User;
import com.epam.ecobites.repository.TokenRepository;
import com.epam.ecobites.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class AuthenticationServiceTest {

	@InjectMocks
	private AuthenticationService authenticationService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private TokenRepository tokenRepository;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JwtService jwtService;

	@Mock
	private UserDetailsServiceImp userDetailsService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testAuthenticate() {
		User user = new User();
		user.setUsername("username");
		user.setPassword("password");

		UserDetails userDetails = mock(UserDetails.class);
		when(userDetailsService.loadUserByUsername(user.getUsername())).thenReturn(userDetails);
		when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
		when(jwtService.generateAccessToken(user)).thenReturn("accessToken");

		AuthenticationResponse response = authenticationService.authenticate(user);

		assertEquals("accessToken", response.getAccessToken());
		verify(authenticationManager, times(1)).authenticate(any());
		verify(tokenRepository, times(1)).save(any());
	}

	@Test
	void testAuthenticateWithException() {
		User user = new User();
		user.setUsername("username");
		user.setPassword("password");

		when(userDetailsService.loadUserByUsername(user.getUsername())).thenThrow(new UsernameNotFoundException("User not found"));

		AuthenticationResponse response = authenticationService.authenticate(user);

		assertEquals("Authentication failed: User not found", response.getMessage());
	}



	@Test
	void testLogout() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);

		when(request.getHeader(anyString())).thenReturn("Bearer token");

		User user = new User();
		user.setId(1);
		when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

		Token token = new Token();
		token.setLoggedOut(false);
		when(tokenRepository.findByAccessToken(anyString())).thenReturn(Optional.of(token));

		authenticationService.logout(request, response, null);

		verify(tokenRepository, times(1)).delete(token);
	}

	@Test
	void testLogoutWithException() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);

		when(request.getHeader(anyString())).thenReturn("Bearer token");

		User user = new User();
		user.setId(1);
		when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

		when(tokenRepository.findByAccessToken(anyString())).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> authenticationService.logout(request, response, null));
	}

	@Test
	void testLogoutWithNoTokenFound() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);

		when(request.getHeader(anyString())).thenReturn("Bearer token");
		when(tokenRepository.findByAccessToken(anyString())).thenReturn(Optional.empty());

		assertThrows(InvalidTokenException.class, () -> authenticationService.logout(request, response, null));
	}

	@Test
	void testLogoutWithNoToken() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);

		when(request.getHeader(anyString())).thenReturn(null);

		assertThrows(InvalidTokenException.class, () -> authenticationService.logout(request, response, null));
	}

	@Test
	void testLogoutWithNoUser() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);

		when(request.getHeader(anyString())).thenReturn("Bearer token");
		when(jwtService.extractUsername(anyString())).thenReturn("username");

		Token mockToken = new Token();
		when(tokenRepository.findByAccessToken(anyString())).thenReturn(Optional.of(mockToken));

		authenticationService.logout(request, response, null);

		verify(tokenRepository, never()).findAllAccessTokensByUser(anyInt());
	}


	@Test
	void testLogoutWithNoValidTokens() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);

		when(request.getHeader(anyString())).thenReturn("Bearer token");

		Token mockToken = new Token();
		when(tokenRepository.findByAccessToken(anyString())).thenReturn(Optional.of(mockToken));

		authenticationService.logout(request, response , null);

		verify(tokenRepository, times(1)).delete(mockToken);
	}

	@Test
	void testLogoutWithValidTokens() {
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);

		when(request.getHeader(anyString())).thenReturn("Bearer token");

		Token token1 = new Token();
		token1.setLoggedOut(false);
		Token token2 = new Token();
		token2.setLoggedOut(false);

		when(tokenRepository.findByAccessToken(anyString())).thenReturn(Optional.of(token1), Optional.of(token2));

		authenticationService.logout(request, response , null);

		token1.setLoggedOut(true);
		token2.setLoggedOut(true);

		verify(tokenRepository, times(1)).delete(token1);
		verify(tokenRepository, times(1)).delete(token2);      
	}


	@Test
	void testRevokeAllTokenByUser() {
		User user = new User();
		user.setId(1);

		Token token1 = new Token();
		token1.setLoggedOut(false);
		Token token2 = new Token();
		token2.setLoggedOut(false);

		List<Token> tokens = Arrays.asList(token1, token2);
		when(tokenRepository.findAllAccessTokensByUser(user.getId())).thenReturn(tokens);

		authenticationService.revokeAllTokenByUser(user);

		verify(tokenRepository, times(1)).saveAll(tokens);
	}

	@Test
	void testSaveUserToken() {
		User user = new User();
		user.setId(1);

		Token token = new Token();
		token.setAccessToken("accessToken");
		token.setLoggedOut(false);
		token.setUser(user);

		when(tokenRepository.save(any(Token.class))).thenReturn(token);

		authenticationService.saveUserToken("accessToken", user);

		verify(tokenRepository, times(1)).save(token);
	}
}
