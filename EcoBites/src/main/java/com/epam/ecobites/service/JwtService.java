package com.epam.ecobites.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.epam.ecobites.model.User;
import com.epam.ecobites.repository.TokenRepository;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

	@Value("${application.security.jwt.secret-key}")
	private String secretKey; 


	@Value("${application.security.jwt.access-token-expiration}")
	private long accessTokenExpire;

	@Value("${application.security.jwt.refresh-token-expiration}")
	private long refreshTokenExpire;

	@Autowired
	private TokenRepository tokenRepository;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public boolean isValid(String token, UserDetails user) {
		String username = extractUsername(token);

		boolean validToken = tokenRepository
				.findByAccessToken(token)
				.map(t -> !t.isLoggedOut())
				.orElse(false);

		return (username.equals(user.getUsername())) && !isTokenExpired(token) && validToken;
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> resolver) {
		Claims claims = extractAllClaims(token);
		return resolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSigninKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	public String generateAccessToken(User user) {
		return generateToken(user, accessTokenExpire);
	}

	private String generateToken(User user, long expireTime) {
		return Jwts
				.builder()
				.setSubject(user.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expireTime ))
				.signWith(getSigninKey())
				.compact();
	}

	private SecretKey getSigninKey() {
		byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
