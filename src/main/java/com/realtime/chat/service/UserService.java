package com.realtime.chat.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.realtime.chat.model.User;
import com.realtime.chat.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	private final UserRepository repo;
	private final PasswordEncoder encoder;
	
	public UserService(UserRepository repo, PasswordEncoder encoder) {
		this.repo = repo;
		this.encoder = encoder;
	}
	
	public User register(String username, String password) {
		if(repo.existsByUsername(username)) {
			throw new RuntimeException("Username already exists");
		}
		User user = new User(username, encoder.encode(password));
		return repo.save(user);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User u = repo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Not Found"));
		
		return new org.springframework.security.core.userdetails.User(u.getUsername(), u.getPassword(), Collections.emptyList());
	}
	
}
