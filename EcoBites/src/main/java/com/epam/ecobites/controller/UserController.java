package com.epam.ecobites.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epam.ecobites.entity.User;
import com.epam.ecobites.model.UserDto;
import com.epam.ecobites.service.UserService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<User> addUser(@RequestBody @Valid UserDto userDto) {
		return new ResponseEntity<>(userService.registerUser(userDto), HttpStatus.CREATED);
	}

}
