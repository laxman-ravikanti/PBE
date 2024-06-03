package com.epam.ecobites.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.epam.ecobites.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {


}
