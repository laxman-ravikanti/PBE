package com.epam.ecobites;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.epam.ecobites.entity.User;
import com.epam.ecobites.exception.InValidEmailID;
import com.epam.ecobites.exception.InValidPassword;
import com.epam.ecobites.exception.InvalidCredentials;
import com.epam.ecobites.exception.ValidEmailID;
import com.epam.ecobites.model.LoginDTO;
import com.epam.ecobites.repository.UserRepository;
import com.epam.ecobites.service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ServiceTestCases {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private UserServiceImpl userService;

	@Test
	public void checkLoginDetails_userNotRegistered_shouldThrowInValidEmailIdException() {
		LoginDTO loginDto = new LoginDTO();
		loginDto.setEmailId("test@gmail.com");
		loginDto.setPassword("test");

		when(userRepository.findByEmailId(loginDto.getEmailId())).thenReturn(Optional.empty());

		InValidEmailID exception = assertThrows(InValidEmailID.class, () -> userService.checkLoginDetails(loginDto));

		verify(userRepository, times(1)).findByEmailId(loginDto.getEmailId());
		verify(passwordEncoder, never()).matches(anyString(), anyString());

		assertEquals("Exception: User is not registered", exception.getMessage());
	}

	@Test
	public void checkLoginDetails_wrongPassword_shouldThrowInvalidCredentials() {

		LoginDTO loginDto = new LoginDTO();
		loginDto.setEmailId("test@gmail.com");
		loginDto.setPassword("wrongPassword");

		User user = new User();
		user.setPassword("hashedPassword");

		when(userRepository.findByEmailId(anyString())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(false);

		assertThrows(InValidPassword.class, () -> userService.checkLoginDetails(loginDto));

		verify(userRepository, times(1)).findByEmailId(loginDto.getEmailId());
		verify(passwordEncoder, times(1)).matches(loginDto.getPassword(), user.getPassword());
	}

	@Test
	public void checkLoginDetails_successfulLogin() {
		LoginDTO loginDto = new LoginDTO();
		loginDto.setEmailId("test@gmail.com");
		loginDto.setPassword("correctPassword");

		User user = new User();
		user.setPassword("hashedCorrectPassword");

		when(userRepository.findByEmailId(loginDto.getEmailId())).thenReturn(Optional.of(user));
		when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(true);

		userService.checkLoginDetails(loginDto);

		verify(userRepository, times(1)).findByEmailId(loginDto.getEmailId());
		verify(passwordEncoder, times(1)).matches(loginDto.getPassword(), user.getPassword());
	}

	@Test
	public void forgetPasswordDetails_ValidEmailId_UserIsRegistered() {
		String email = "test@gmail.com";

		when(userRepository.findByEmailId(email)).thenReturn(Optional.of(new User()));

		// No exception should be thrown if the email ID is valid and the user is
		// registered
		assertDoesNotThrow(() -> userService.forgetPasswordDetails(email));

		verify(userRepository, times(1)).findByEmailId(email);
	}

	@Test
	public void forgetPasswordDetails_InValidEmailId_UserNotRegistered() {
		String email = "test@gmail.com";
		when(userRepository.findByEmailId(email)).thenReturn(Optional.empty());

		// Expect a ResponseEntity with a specific message if the email ID is not
		// registered
		ResponseEntity<String> response = userService.forgetPasswordDetails(email);
		assertEquals("User is not registered", response.getBody());

		verify(userRepository, times(1)).findByEmailId(email);
	}

}
