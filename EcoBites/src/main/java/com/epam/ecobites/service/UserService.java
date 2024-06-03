package com.epam.ecobites.service;


import java.util.List;

import org.springframework.http.ResponseEntity;

import com.epam.ecobites.exception.InvalidCredentials;
import com.epam.ecobites.model.LoginDTO;
import com.epam.ecobites.entity.User;
import com.epam.ecobites.model.UserDto;


public interface UserService {
	
	List<UserDTO> getAllUsers();
	
	UserDTO getUser(int id);
	
		User registerUser(UserDto userDto);

	boolean checkLoginDetails(LoginDTO loginDto) ;

	ResponseEntity<String> forgetPasswordDetails(String emailId);

	
}
