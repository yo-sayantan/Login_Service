package com.orderingApp.auth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.orderingApp.auth.entity.User;
import com.orderingApp.auth.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public void registerUser(User user) {
		registerUser(user.getUsername(), user.getPassword());
	}
    
    private void registerUser(String username, String rawPassword) {
        String encryptedPassword = passwordEncoder.encode(rawPassword);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        userRepository.saveAndFlush(user);
    }
    
    public void saveUser(User user) {
        userRepository.save(user);
    }
    
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
    
	@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	User user;
    	
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