package com.realtime.chat.controller;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.realtime.chat.model.ChatMessage;
import com.realtime.chat.repository.ChatMessageRepository;
import com.realtime.chat.utility.JwtUtil;

@Controller
public class ChatController {
	
	@Autowired
	private ChatMessageRepository messageRepo;
	
	@Autowired
	private JwtUtil jwtUtil;
		
	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public ChatMessage recieve(ChatMessage message, Principal auth, SimpMessageHeaderAccessor headerAccessor) {
	    String username = "Unknown";

	    if (auth != null) {
	        username = auth.getName();
	    } else {
	    	if (headerAccessor != null) {
	            String authHeader = headerAccessor.getFirstNativeHeader("Authorization");
	            if (authHeader != null && authHeader.startsWith("Bearer ")) {
	                try {
	                    String token = authHeader.substring(7);
	                    username = jwtUtil.extractUsername(token);
	                } catch (Exception e) {
	                    System.out.println("❌ Error extracting username from token: " + e.getMessage());
	                }
	            }
	        }
	    }
        ChatMessage saved = new ChatMessage(username, message.getText(), LocalDateTime.now());
        messageRepo.save(saved);
        return saved;
	}
}
