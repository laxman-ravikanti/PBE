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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private  UserDetailsServiceImp userDetailsServiceImp;

	@Autowired
	private  JwtAuthenticationFilter jwtAuthenticationFilter;

	@Autowired
	private CustomAuthEntryPoint authEntryPoint;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(
						req->req.requestMatchers("/users/login/**","/users/register/**", "/error/**")
						.permitAll()
						.requestMatchers("/admin_only/**").hasAuthority("ADMIN")
						.anyRequest()
						.authenticated())
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
