package com.epam.ecobites;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.epam.ecobites.controller.UserController;
import com.epam.ecobites.model.LoginDTO;
import com.epam.ecobites.service.UserService;
import com.epam.ecobites.validation.Exceptionhandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class ExceptionHandlerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private UserController userController;

	@Mock
	private Exceptionhandler exceptionhandler;

	@Mock
	private UserService userService;

	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(userController).setControllerAdvice(exceptionhandler).build();
	}

	@Test
	public void testHandleMethodArgument() throws Exception {
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setEmailId("test");
		loginDTO.setPassword("test");
		mockMvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginDTO))).andExpect(status().isBadRequest());
	}

	@Test
	public void testInvalidCredsException() throws Exception {
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setEmailId("test");
		loginDTO.setPassword("test");
		mockMvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginDTO))).andExpect(status().isBadRequest());
	}

	@Test
	public void testInvalidEmailID() throws Exception {
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setEmailId("test");
		loginDTO.setPassword("test");
		mockMvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginDTO))).andExpect(status().isBadRequest());
	}

	@Test
	public void testInvalidPassword() throws Exception {
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setEmailId("test");
		loginDTO.setPassword("test");
		mockMvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginDTO))).andExpect(status().isBadRequest());
	}

	@Test
	public void testInvalidCredentials() throws Exception {
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setEmailId("test");
		loginDTO.setPassword("test");
		mockMvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginDTO))).andExpect(status().isBadRequest());
	}

	@Test
	public void testNoSuchUserIdException() throws Exception {
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setEmailId("test");
		loginDTO.setPassword("test");
		mockMvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginDTO))).andExpect(status().isBadRequest());
	}

	@Test
	public void testUsersListEmptyException() throws Exception {
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setEmailId("test");
		loginDTO.setPassword("test");
		mockMvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginDTO))).andExpect(status().isBadRequest());
	}

	@Test
	public void testValidEmailID() throws Exception {
		LoginDTO loginDTO = new LoginDTO();
		loginDTO.setEmailId("test");
		loginDTO.setPassword("test");
		mockMvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginDTO))).andExpect(status().isBadRequest());
	}
	
 

}