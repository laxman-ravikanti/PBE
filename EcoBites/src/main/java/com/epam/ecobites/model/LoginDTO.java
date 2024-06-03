package com.epam.ecobites.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDTO {

	@NotBlank(message = "EmailId should not be empty")
	@Email
    private String emailId ;
	
	@NotBlank(message = "password should not be empty")
    private String password;
    
}
