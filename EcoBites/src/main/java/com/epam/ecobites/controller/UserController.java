package com.epam.ecobites.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.epam.ecobites.entity.User;
import com.epam.ecobites.model.LoginDTO;
import com.epam.ecobites.model.UserDTO;
import com.epam.ecobites.service.JwtServiceImpl;
import com.epam.ecobites.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtServiceImpl jwtServiceImpl;

	@Autowired
	private AuthenticationManager authenticationManager;

	@PostMapping("/new")
	public String addNewUser(@RequestBody @Valid User user) {
		System.out.println(user.getEmailId() + " " + user.getPassword());
		return userService.addUser(user);
	}

	@GetMapping("/all")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public List<UserDTO> getAllTheUsers() {
		return userService.getAllUsers();
	}

	@PreAuthorize("hasAuthority('ROLE_USER')")
	@GetMapping("/{id}")
	public UserDTO getUserById(@PathVariable(name = "id") int id) {
		System.out.println(id);
		return userService.getUser(id);
	}

	@PostMapping("/login")
	public ResponseEntity<String> authenticateAndGetToken(@RequestBody @Valid LoginDTO authRequest) {
	    
	    boolean validDetails = userService.checkLoginDetails(authRequest);

	    if (!validDetails) {
	        return new ResponseEntity<>("Invalid credentials. Unable to authenticate user.", HttpStatus.BAD_REQUEST);
	    }

	    Authentication authentication = authenticationManager.authenticate(
	        new UsernamePasswordAuthenticationToken(authRequest.getEmailId(), authRequest.getPassword()));

	    if (authentication.isAuthenticated()) {
	        String token = jwtServiceImpl.generateToken(authRequest.getEmailId());
	        return new ResponseEntity<>(token, HttpStatus.OK);
	    }

	    return new ResponseEntity<>("Invalid credentials. Unable to authenticate user.", HttpStatus.BAD_REQUEST);
	}
	
	 
	
//	@PostMapping("/login")
//	public String authenticateAndGetToken(@RequestBody @Valid LoginDTO authRequest) throws InvalidCredentials {
//		userService.checkLoginDetails(authRequest);
//		Authentication authentication = authenticationManager.authenticate(
//				new UsernamePasswordAuthenticationToken(authRequest.getEmailId(), authRequest.getPassword()));
//
//		if (authentication.isAuthenticated()) {
//			String token = jwtServiceImpl.generateToken(authRequest.getEmailId());
//			if (token != null) {
//				return "user loggedin successfully";
//			}
//			else {
//				throw new InvalidCredentials("user login failed");
//			}
//		}
//
//		throw new InvalidCredentials("not valid creds");
//	}
	 

	@PostMapping("/forgetpassword/{emailId}")
	public ResponseEntity<String> forgetPassword(@PathVariable("emailId") String emailId){ 
		return userService.forgetPasswordDetails(emailId);  
	}
}

















//if (userRepository.findByName(authRequest.getUsername()) != null) {
//
//	Authentication authentication = authenticationManager.authenticate(
//			new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
//
//	if (authentication.isAuthenticated()) {
//		return jwtServiceImpl.generateToken(authRequest.getUsername());
//	}
//} 
//throw new UsernameNotFoundException("Invalid user request !");