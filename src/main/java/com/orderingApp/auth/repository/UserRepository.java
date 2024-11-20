package com.orderingApp.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.orderingApp.auth.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
//    Optional<User> findByUsername1(String username);

	UserDetails findByUsername(String username);
}