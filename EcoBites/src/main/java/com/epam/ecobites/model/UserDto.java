package com.epam.ecobites.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
	@NotBlank(message = "User Name should not be blank or null")
	@Size(min = 3, max = 20, message = "User Name should be between 3 to 20 characters")
	@Pattern(regexp = "^[A-Za-z][A-Za-z0-9@_.-]{3,20}$", message = "Invalid user name format should start with [A-Za-z] and should be 3 to 20 characters long")
	private String userName;

	@NotBlank(message = "Email should not be blank or null")
	@Pattern(regexp = "^[A-Za-z0-9.]{5,30}+@[A-Za-z.]+\\.[A-Za-z]{2,6}$", message = "Invalid email format")
	private String email;

	@NotBlank(message = "Password should not be blank or null")
	@Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=\\S+$).{8,20}$", message = "Invalid password format should contain [A-Z], [a-z], [0-9] and special characters should be 8 to 20 characters long")
	private String password;

}
