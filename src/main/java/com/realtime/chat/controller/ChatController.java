package com.realtime.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.realtime.chat.model.ChatMessage;

@Controller
public class ChatController {
	
	// When client send message to /app/chat.sendMesage, this method runs
//	@MessageMapping("/chat.sendMessage")
//	@SendTo("/topic/public")
//	public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
//		return chatMessage;
//	}
	
	@MessageMapping("/hello")
	@SendTo("/topic/greetings")
	public ChatMessage greeting(ChatMessage chatMessage) throws Exception {
		System.out.println("Recieved message: "+chatMessage.getText());
		return new ChatMessage("Server","Echo: "+chatMessage.getText(),chatMessage.getTime());
	}
}
