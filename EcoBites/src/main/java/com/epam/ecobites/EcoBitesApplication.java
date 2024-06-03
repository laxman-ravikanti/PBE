package com.epam.ecobites;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })

public class EcoBitesApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcoBitesApplication.class, args);
	}
}
