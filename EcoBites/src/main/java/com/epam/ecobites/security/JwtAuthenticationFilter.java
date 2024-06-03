package com.epam.ecobites.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.epam.ecobites.service.JwtService;
import com.epam.ecobites.service.UserDetailsServiceImp;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserDetailsServiceImp userDetailsService;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain)throws ServletException, IOException {

		String requestURI = request.getRequestURI();

		if (requestURI.contains("/error/invalid_token")) {
			filterChain.doFilter(request, response);
			return;
		}

		String authHeader = request.getHeader("Authorization");

		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request,response);
			return;
		}

		String token = authHeader.substring(7);
		int periodCount = 0;
		for (char c : token.toCharArray()) {
			if (c == '.') {
				periodCount++;
			}
		}

		if (token != null && periodCount == 2) {
			String username = jwtService.extractUsername(token);

			if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				if(jwtService.isValid(token, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				} else {
					response.sendRedirect("/error/invalid_token");
					return;
				}
			}
		} 
		else {
			response.sendRedirect("/error/invalid_token");
			return;
		}
		filterChain.doFilter(request, response);
	}
}



