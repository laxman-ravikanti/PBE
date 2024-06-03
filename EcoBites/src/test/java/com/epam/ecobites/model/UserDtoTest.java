package com.epam.ecobites.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserDtoTest {

    
	
	@Test
	void testUserNamePattern() {
		String pattern = "[A-Za-z][A-Za-z0-9@_.-]{3,20}";
		UserDto userDto = new UserDto();
		userDto.setUserName("userTest");
		assertThat(userDto.getUserName()).matches(pattern);
		userDto.setUserName("123user");
		assertThat(userDto.getUserName()).doesNotMatch(pattern);
	}

	@Test
	void testEmailPattern() {
		String pattern = "^[A-Za-z0-9.]{5,30}+@[A-Za-z.]+\\.[A-Za-z]{2,6}$";
		UserDto userDto = new UserDto();
		userDto.setEmail(".ejei@com");
		assertThat(userDto.getEmail()).doesNotMatch(pattern);
		userDto.setEmail("test34@gmail.com");
		assertThat(userDto.getEmail()).matches(pattern);
	}

	@Test
	void testPasswordPattern() {
		String passwordPattern = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=\\S+$).{8,20}$";
		UserDto userDto = new UserDto();
		userDto.setPassword("password");
		assertThat(userDto.getPassword()).doesNotMatch(passwordPattern);
		userDto.setPassword("Password@123");
		assertThat(userDto.getPassword()).matches(passwordPattern);
	}

}
