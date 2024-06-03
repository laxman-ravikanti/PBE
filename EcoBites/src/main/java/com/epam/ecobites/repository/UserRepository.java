package com.epam.ecobites.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.ecobites.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {


	Optional<User> findByEmailId(String username);

}
