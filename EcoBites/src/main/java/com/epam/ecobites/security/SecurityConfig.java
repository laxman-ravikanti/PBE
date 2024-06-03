package com.epam.ecobites.security;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import com.epam.ecobites.service.UserDetailsServiceImp;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private  UserDetailsServiceImp userDetailsServiceImp;

	@Autowired
	private  JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	
	public UserDetailsService userDetailsService() {
		return new UserInfoUserDetailsService();
	}
  
  
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("users/**").allowedOrigins("http://localhost:3000")
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedHeaders("*")
						.allowCredentials(true);
			}

		};
  }


	@Autowired
	private CustomAuthEntryPoint authEntryPoint;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(AbstractHttpConfigurer::disable)
      .authorizeHttpRequests(requests -> requests.requestMatchers("/users/new", "/users/login" , "/users/forgetpassword/**","/error/**")
				.permitAll())
				.authorizeHttpRequests(requests -> requests.requestMatchers("/users/**").authenticated())
				.userDetailsService(userDetailsServiceImp)
				.sessionManagement(session->session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(
						e->e.accessDeniedHandler(
								(request, response, accessDeniedException)->response.setStatus(403)
								)
						.authenticationEntryPoint(authEntryPoint)) 
				.logout(l->l
						.logoutUrl("/logout")
						.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()
								))
				.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean

	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

}
