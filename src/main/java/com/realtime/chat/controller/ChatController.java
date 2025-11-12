package com.realtime.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.realtime.chat.model.ChatMessage;

@Controller
public class ChatController {
		
	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public ChatMessage greeting(ChatMessage message, Authentication auth) {
		String username = auth != null ? auth.getName() : "Anonymous";
        System.out.println("Received from " + username + ": " + message.getText());
        return new ChatMessage(username, message.getText(), message.getTime());
	}
}
