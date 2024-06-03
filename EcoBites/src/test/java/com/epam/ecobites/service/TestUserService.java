package com.epam.ecobites.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.epam.ecobites.entity.Role;
import com.epam.ecobites.entity.User;
import com.epam.ecobites.exception.UserExistException;
import com.epam.ecobites.model.UserDto;
import com.epam.ecobites.repository.UserRepository;

class TestUserService {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserServiceImplementation userService;

	@Mock
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

	}

	@Test
	void testRegisterUserThrowsException() {
		UserDto userDto = UserDto.builder().userName("test").email("proya@email.com").password("password").build();

		User user = User.builder().userName(userDto.getUserName()).email(userDto.getEmail().toLowerCase())
				.password(userDto.getPassword()).dateCreated(new Date()).build();
		when(userRepository.findAll()).thenReturn(Arrays.asList(user));

		assertThrows(UserExistException.class, () -> userService.registerUser(userDto));
	}

	@Test
	void testRegisterUserSuccess() {
		UserDto userDto = UserDto.builder().userName("test").email("proya@email.com").password("password").build();

		User user = User.builder().userName(userDto.getUserName()).email(userDto.getEmail().toLowerCase())
				.password(userDto.getPassword()).dateCreated(new Date()).role(Role.USER).build();
		User user1 = User.builder().id(1).userName("test1").email("bobby@gmail.com").password("password")
				.dateCreated(new Date()).build();
		when(userRepository.findAll()).thenReturn(Arrays.asList(user1));

		when(userRepository.save(any(User.class))).thenReturn(user);

		assertEquals(user, userService.registerUser(userDto));
	}

	@Test
	void testRegisterUserException() {
		UserDto userDto = UserDto.builder().userName("test").email("suchi@gmail.com").password("password").build();
		User user = User.builder().userName("tese").email(userDto.getEmail().toLowerCase())
				.password(userDto.getPassword()).dateCreated(new Date()).role(Role.USER).build();
		when(userRepository.findAll()).thenReturn(Arrays.asList(user));
		assertThrows(UserExistException.class, () -> userService.registerUser(userDto));
	}


	

	
	
}
