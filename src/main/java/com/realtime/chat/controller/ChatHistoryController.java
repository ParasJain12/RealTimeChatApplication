package com.realtime.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realtime.chat.model.ChatMessage;
import com.realtime.chat.repository.ChatMessageRepository;

@RestController
@RequestMapping("/api/chat")
public class ChatHistoryController {

	@Autowired
	ChatMessageRepository messageRepo;
	
	@GetMapping("/history")
	public List<ChatMessage> history(){
		return messageRepo.findAll();
	}
	
}
