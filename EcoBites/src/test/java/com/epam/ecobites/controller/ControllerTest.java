package com.epam.ecobites.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.epam.ecobites.entity.Role;
import com.epam.ecobites.entity.User;
import com.epam.ecobites.exception.ExceptionResponse;
import com.epam.ecobites.exception.UserExistException;
import com.epam.ecobites.model.UserDto;
import com.epam.ecobites.service.UserService;
import com.epam.ecobites.service.UserServiceImplementation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

@ExtendWith(MockitoExtension.class)
class ControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	UserController userController;

	@Mock
	UserService userService = new UserServiceImplementation();

	@Mock
	ExceptionResponse exceptionResponse;

	@Mock
	GlobalExceptionHandler exceptionHandler;

	UserDto userDto = UserDto.builder().userName("John").email("harsh@gmail.com").password("Efff@j12").build();

	ObjectMapper objectMapper = new ObjectMapper();
	ObjectWriter objectWriter = objectMapper.writer();

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(userController).setControllerAdvice(new GlobalExceptionHandler())
				.build();

	}

	@Test
	void testUserRegistration() throws Exception {
		User user = User.builder().userName("John").email("harsh@gmail.com").password("Efff@j12").image("image")
				.role(Role.USER).build();
		when(userService.registerUser(userDto)).thenReturn(user);

		mockMvc.perform(MockMvcRequestBuilders.post("/users/register").contentType(MediaType.APPLICATION_JSON)
				.content(objectWriter.writeValueAsString(userDto))).andExpect(status().isCreated());

	}

	@Test
	void testUserRegistrationException() throws Exception {

		when(userService.registerUser(userDto)).thenThrow(new UserExistException("User already exist"));

		mockMvc.perform(post("/users/register").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(userDto))).andExpect(status().isConflict());
	}

	@Test
	void testAddUserInvalidUsername() throws Exception {
		UserDto userDto = new UserDto();
		userDto.setUserName("us");

		mockMvc.perform(post("/users/register").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(userDto))).andExpect(status().isBadRequest());
	}

	@Test
	void testAddUserInvalidEmail() throws Exception {
		UserDto userDto = new UserDto();
		userDto.setUserName("user");
		userDto.setEmail("user.com");
		mockMvc.perform(post("/users/register").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(userDto))).andExpect(status().isBadRequest());
	}

	@Test
	void testAddUserInvalidPassword() throws Exception {
		UserDto userDto = new UserDto();
		userDto.setUserName("user");
		userDto.setEmail("harshi@gmail.com");
		userDto.setPassword("password");
		mockMvc.perform(post("/users/register").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(userDto))).andExpect(status().isBadRequest());
	}
    
	
	@Test
	void  validUsername() throws Exception {
		UserDto userDto = new UserDto();
		userDto.setUserName("user123");
		userDto.setEmail("userr@gmail.com");
		userDto.setPassword("Password@123");

		mockMvc.perform(post("/users/register").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(userDto))).andExpect(status().isCreated());
	}
	

}
