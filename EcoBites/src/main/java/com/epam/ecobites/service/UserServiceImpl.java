package com.epam.ecobites.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.epam.ecobites.entity.User;
import com.epam.ecobites.exception.InValidEmailID;
import com.epam.ecobites.exception.InValidPassword;
import com.epam.ecobites.exception.InvalidCredentials;
import com.epam.ecobites.exception.NoSuchUserIdException;
import com.epam.ecobites.exception.UsersListEmptyException;
import com.epam.ecobites.model.LoginDTO;
import com.epam.ecobites.model.UserDTO;
import com.epam.ecobites.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	List<UserDTO> usersList = null;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public String addUser(User userInfo) {
		userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
		userRepository.save(userInfo);
		return "user added to system ";
	}

	public List<UserDTO> getAllUsers() {
		List<User> users = userRepository.findAll();

		if (users.isEmpty()) {
			throw new UsersListEmptyException("No users found in the database");
		}

		return users.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	private UserDTO convertToDto(User user) {
		UserDTO userDto = new UserDTO(); 
		userDto.setName(user.getName());
		userDto.setEmailId(user.getEmailId());
		userDto.setPassword(user.getPassword());
		userDto.setRoles(user.getRoles());
		return userDto;
	}

	public boolean checkLoginDetails(LoginDTO loginDto) {
		boolean bool = true;
		Optional<User> userOptional = userRepository.findByEmailId(loginDto.getEmailId());

		if (!userOptional.isPresent()) {
			bool = false;
			throw new InValidEmailID("User is not registered");
		}

		User user = userOptional.get();

		if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
			bool = false;
			throw new InValidPassword("Password is wrong");
		}
		 return bool;
	} 
 

	public ResponseEntity<String> forgetPasswordDetails(String emailId) {
		Optional<User> userOptional = userRepository.findByEmailId(emailId);
		if (!userOptional.isPresent()) {
			return new ResponseEntity<>("User is not registered", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>("User is registered, we can send reset password link to email id",
					HttpStatus.OK);
		}
	}

	public UserDTO getUser(int id) {
		User user = userRepository.findById(id).orElseThrow(() -> new NoSuchUserIdException("No user with ID: " + id));
		return convertToDto(user);
	}

}
