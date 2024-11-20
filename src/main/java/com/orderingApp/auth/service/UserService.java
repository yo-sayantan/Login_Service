package com.orderingApp.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.orderingApp.auth.entity.User;
import com.orderingApp.auth.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

//    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

//    public Optional<User> findByUsername1(String username) {
//        return userRepository.findByUsername1(username);
//    }
    
    public UserDetails findByUsername(String username) {
	    return userRepository.findByUsername(username);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			return userRepository.findByUsername(username);
		} catch (Exception e) {
			throw new UsernameNotFoundException("User not found: " + username);
		}
    }
	
//	@Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findByUsername(username)
//                .map(u -> User.withUsername(u.getUsername())
//                        .password(u.getPassword())
//                        .roles(u.getRoles()) // map roles appropriately
//                        .build())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//    }
	
//	private UserDetails extractUserDetails(User user) {
//		MapRoleUser mapRoleUser = mapRoleUserRepo.findByUser(user);
//		return new UserPrincipal(user.getId(), user.getUsername(), user.getPassword(),
//				mapRoleUser.getRole().getRoleName(), user.getFullname());
//	}
}