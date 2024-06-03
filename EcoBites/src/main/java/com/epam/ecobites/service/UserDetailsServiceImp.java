package com.epam.ecobites.service;

import com.epam.ecobites.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.epam.ecobites.exception.InvalidTokenException;
import com.epam.ecobites.model.User;
import com.epam.ecobites.repository.UserRepository;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByUsername(username).orElseThrow(() -> new InvalidTokenException("User not found with username: " + username));
		return new CustomUserDetails(user);
	}
}
