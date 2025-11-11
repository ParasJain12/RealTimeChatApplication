package com.realtime.chat.controller;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realtime.chat.model.User;
import com.realtime.chat.service.UserService;
import com.realtime.chat.utility.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {
	
	private final AuthenticationManager authManager;
	private final JwtUtil jwtUtil;
	private final UserService userService;
	
	public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil, UserService userService) {
		super();
		this.authManager = authManager;
		this.jwtUtil = jwtUtil;
		this.userService = userService;
	}
	
	@PostMapping("/register")
	public User register(@RequestBody Map<String,String> body) {
		return userService.register(body.get("username"), body.get("password"));
	}
	
	@PostMapping("/login")
	public Map<String,String> login(@RequestBody Map<String,String> body){
		Authentication auth = authManager.authenticate(
				new UsernamePasswordAuthenticationToken(body.get("username"),body.get("password")));
		
		String token = jwtUtil.generateToken(auth.getName());
		return Map.of("token",token);
	}
}
