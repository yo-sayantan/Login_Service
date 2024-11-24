package com.orderingApp.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orderingApp.auth.entity.Users;


@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findUserByUsername(String username);
    
    Users findByUsername(String username);
    Users findByEmail(String email);
    
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}