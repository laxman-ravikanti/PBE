package com.epam.ecobites.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.epam.ecobites.exception.AuthenticationResponse;
import com.epam.ecobites.exception.InvalidTokenException;
import com.epam.ecobites.model.Token;
import com.epam.ecobites.model.User;
import com.epam.ecobites.repository.TokenRepository;
import com.epam.ecobites.repository.UserRepository;

import java.util.List;

@Service
public class AuthenticationService implements LogoutHandler {

	@Autowired
	private  UserRepository repository;

	@Autowired
	private  JwtService jwtService;
	@Autowired
	private  TokenRepository tokenRepository;
	@Autowired
	private  AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsServiceImp userDetailsService;

	public AuthenticationResponse authenticate(User request) {

		try {
			UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDetails, request.getPassword()));
		} catch (AuthenticationException e) {
			return new AuthenticationResponse(null, "Authentication failed: " + e.getMessage());
		}

		User user = repository.findByUsername(request.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + request.getUsername()));

		String accessToken = jwtService.generateAccessToken(user);

		revokeAllTokenByUser(user);
		saveUserToken(accessToken, user);

		return new AuthenticationResponse(accessToken, "User login was successful");

	}

	void revokeAllTokenByUser(User user) {
		List<Token> validTokens = tokenRepository.findAllAccessTokensByUser(user.getId());
		if(validTokens.isEmpty()) {
			return;
		}

		validTokens.forEach(t-> {
			t.setLoggedOut(true);
		});

		tokenRepository.saveAll(validTokens);
	}

	void saveUserToken(String accessToken, User user) {
		Token token = new Token();
		token.setAccessToken(accessToken);
		token.setLoggedOut(false);
		token.setUser(user);
		tokenRepository.save(token);
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			throw new InvalidTokenException("Missing or Invalid Authorization header.");
		}

		String token = authHeader.substring(7);
		Token storedToken = tokenRepository.findByAccessToken(token)
				.orElseThrow(() -> new InvalidTokenException("No user found with this token"));

		if(storedToken == null || storedToken.isLoggedOut()) {
			throw new InvalidTokenException("Token is invalid or already logged out");
		}
		storedToken.setLoggedOut(true);
		tokenRepository.delete(storedToken);
	}

}

