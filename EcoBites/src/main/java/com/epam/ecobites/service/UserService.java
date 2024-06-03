package com.epam.ecobites.service;

import com.epam.ecobites.entity.User;
import com.epam.ecobites.model.UserDto;

public interface UserService {

	User registerUser(UserDto userDto);
	
}
