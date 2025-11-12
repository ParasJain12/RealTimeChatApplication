package com.realtime.chat.config;

import java.util.Collections;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.realtime.chat.utility.JwtUtil;

import io.jsonwebtoken.JwtException;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {
	
	private final JwtUtil jwtUtil;

	public WebSocketAuthInterceptor(JwtUtil jwtUtil) {
		super();
		this.jwtUtil = jwtUtil;
	}
	
	@Override
	public Message<?> preSend(Message<?> message,MessageChannel channel){
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		String authHeader = accessor.getFirstNativeHeader(org.springframework.http.HttpHeaders.AUTHORIZATION);
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			try {
				String username = jwtUtil.extractUsername(token);
				if(jwtUtil.validateToken(token)) {
					UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(username,null,Collections.emptyList());
					accessor.setUser(userAuth);
					SecurityContextHolder.getContext().setAuthentication(userAuth);
				}
			}catch(JwtException e) {
				System.out.println("Invalid websocket jwt: "+e.getMessage());
				return null;
			}
		}
		return message;
	}
	
}
