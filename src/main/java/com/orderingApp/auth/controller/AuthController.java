package com.orderingApp.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orderingApp.auth.entity.Users;
import com.orderingApp.auth.service.AuthRequest;
import com.orderingApp.auth.service.AuthResponse;
import com.orderingApp.auth.service.UserService;
import com.orderingApp.auth.util.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;
    
	@Autowired
    private UserService userService;
    
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody AuthRequest signupRequest) {
    	Users user = new Users();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(signupRequest.getPassword());
        
        if (signupRequest.getRoles() == null || signupRequest.getRoles().isEmpty())
            return ResponseEntity.badRequest().body("Roles cannot be null or empty.");
        
        if (signupRequest.getEmail() == null || signupRequest.getEmail().isEmpty())
        	return ResponseEntity.badRequest().body("Email cannot be null or empty.");
        
        user.setEmail(signupRequest.getEmail());
    	String response = userService.registerUser(user, signupRequest.getRoles());
    	
    	if(response.equals("SUCCESS"))
    		return ResponseEntity.ok("User is registered successfully");
    	return ResponseEntity.badRequest().body(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        
        if (authentication.isAuthenticated()) {
            String token = jwtUtil.generateToken(authRequest.getUsername());
            return ResponseEntity.ok(new AuthResponse(token));
        } else {
        	return ResponseEntity.badRequest().body("Invalid credentials");
        }
    }
    
    @Autowired
    public AuthController(AuthenticationManager authenticationManager) {
    }
}