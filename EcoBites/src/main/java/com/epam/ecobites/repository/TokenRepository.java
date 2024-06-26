package com.epam.ecobites.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.epam.ecobites.model.Token;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {


	@Query("""
			select t from Token t inner join User u on t.user.id = u.id
			where t.user.id = :userId and t.loggedOut = false
			""")
	List<Token> findAllAccessTokensByUser(@Param("userId") Integer userId);

	Optional<Token> findByAccessToken(String token);

	void deleteByAccessToken(Token storedToken);

}
