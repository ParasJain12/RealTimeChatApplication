package com.realtime.chat.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessage {
	private String from;
	private String text;
	private String time;
	
}
