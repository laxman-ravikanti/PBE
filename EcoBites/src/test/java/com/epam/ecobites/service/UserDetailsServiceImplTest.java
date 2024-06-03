package com.epam.ecobites.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import com.epam.ecobites.exception.InvalidTokenException;
import com.epam.ecobites.model.User;
import com.epam.ecobites.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserDetailsServiceImp userDetailsService;


	@Test
	void loadUserByUsername_UserExists_ReturnsUserDetails() {
		String username = "testUser";
		User mockUser = mock(User.class);

		when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

		UserDetails result = userDetailsService.loadUserByUsername(username);

		assertNotNull(result);
		verify(userRepository, times(1)).findByUsername(username);
	}


	@Test
	void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
		String username = "testUser";

		when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

		assertThrows(InvalidTokenException.class, () -> {
			userDetailsService.loadUserByUsername(username);
		});
	}
}
