package com.epam.ecobites.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.epam.ecobites.exception.AuthenticationResponse;
import com.epam.ecobites.exception.InvalidTokenException;
import com.epam.ecobites.model.User;
import com.epam.ecobites.service.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class AuthenticationController {

	@Autowired
	AuthenticationService authService;


	@PostMapping("users/login")
	public ResponseEntity<AuthenticationResponse> login( @RequestBody User request ) {
		return ResponseEntity.ok(authService.authenticate(request));
	}

	@PostMapping("users/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		authService.logout(request, response,authentication);
		return ResponseEntity.ok("Logout successful");
	}

	@GetMapping("/error/invalid_token")
	public void handleUserNotFoundError() {
		throw new InvalidTokenException("No user found with this token");
	}

}