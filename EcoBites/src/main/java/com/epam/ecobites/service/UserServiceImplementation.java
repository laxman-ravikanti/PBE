package com.epam.ecobites.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.epam.ecobites.entity.Role;
import com.epam.ecobites.entity.User;
import com.epam.ecobites.exception.UserExistException;
import com.epam.ecobites.model.UserDto;
import com.epam.ecobites.repository.UserRepository;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User registerUser(UserDto userDto) {

		userRepository.findAll().forEach(user -> {
			if (user.getUserName().equalsIgnoreCase(userDto.getUserName()))
				throw new UserExistException("UserName already exist");
		});
		userRepository.findAll().forEach(user -> {
			if (user.getEmail().equalsIgnoreCase(userDto.getEmail()))
				throw new UserExistException("Email already exist");
		});

		User user = User.builder().userName(userDto.getUserName()).email(userDto.getEmail().toLowerCase())
				.password(userDto.getPassword()).role(Role.USER).image("default.jpg").build();

		return userRepository.save(user);
	}

}
