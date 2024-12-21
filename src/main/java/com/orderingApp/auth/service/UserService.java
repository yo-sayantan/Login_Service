package com.orderingApp.auth.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.orderingApp.auth.entity.Roles;
import com.orderingApp.auth.entity.Users;
import com.orderingApp.auth.repository.RoleRepository;
import com.orderingApp.auth.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public String registerUser(Users user, Set<String> roleNames) {        
        try {
	        if (userRepository.existsByUsername(user.getUsername()))
	            return "Username already exists.";
	        if (userRepository.existsByEmail(user.getEmail()))
	            return "Email already exists.";
	
	        Set<Roles> roles = roleNames.stream()
	        							.map(roleName -> roleRepository.findByName(roleName).orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
	        							.collect(Collectors.toSet());
	        
	        user.setUsername(user.getUsername());
	        user.setPassword(passwordEncoder.encode(user.getPassword()));
	        user.setRoles(roles);
	        userRepository.saveAndFlush(user);
	        
	        return "SUCCESS";
        } catch (Exception e) {
        	return e.getMessage();
        }
    }
    
    public void saveUser(Users user) {
        userRepository.save(user);
    }
    
    public Users findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<Users> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
    
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	Users user;
    	
    	try {
    		user = userRepository.findByUsername(username);
        } catch (Exception e) {
        	throw new UsernameNotFoundException("User not found");
        }
        
    	return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
	
	public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}