package com.realtime.chat.controller;

import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.realtime.chat.model.ChatMessage;
import com.realtime.chat.repository.ChatMessageRepository;

@Controller
public class ChatController {
	
	@Autowired
	private ChatMessageRepository messageRepo;
		
	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public ChatMessage recieve(ChatMessage message, Principal auth) {
		String username = auth != null ? auth.getName() : "Unknown";
        ChatMessage saved = new ChatMessage(username, message.getText(),LocalDateTime.now());
        messageRepo.save(saved);
        return saved;
	}
}
