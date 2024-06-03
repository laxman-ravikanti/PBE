package com.epam.ecobites.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.epam.ecobites.entity.User;
import com.epam.ecobites.exception.InvalidCredentials;
import com.epam.ecobites.model.LoginDTO;
import com.epam.ecobites.model.UserDTO;

public interface UserService {
	
	List<UserDTO> getAllUsers();
	
	UserDTO getUser(int id);
	
	String addUser(User userInfo);

	boolean checkLoginDetails(LoginDTO loginDto) ;

	ResponseEntity<String> forgetPasswordDetails(String emailId);
	
}
