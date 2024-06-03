package com.epam.ecobites.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

	
	@NotBlank(message = "name should not be empty")
	private String name;
	
	@NotBlank(message = "email should not be empty")
	@Email
	private String emailId;
	
	@NotBlank(message = "password should not be empty")
	private String password;
	private String roles;
	
}
