package com.realtime.chat.config;

import java.util.Collections;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import com.realtime.chat.utility.JwtUtil;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    public WebSocketAuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            accessor = StompHeaderAccessor.wrap(message);
        }
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return message;
            }

            String token = authHeader.substring(7);
      
            try {
                String username = jwtUtil.extractUsername(token);

                if (jwtUtil.validateToken(token)) {

                    UsernamePasswordAuthenticationToken userAuth =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    Collections.emptyList()
                            );

                    accessor.setUser(userAuth);
                } else {
                    System.out.println("[WS] Invalid token ❌");
                }

            } catch (Exception e) {
                return null;
            }
        }

        if (StompCommand.SEND.equals(accessor.getCommand())) {
            if (accessor.getUser() == null) {
                
                String authHeader = accessor.getFirstNativeHeader("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    try {
                        String username = jwtUtil.extractUsername(token);
                        if (jwtUtil.validateToken(token)) {
                            UsernamePasswordAuthenticationToken userAuth =
                                    new UsernamePasswordAuthenticationToken(
                                            username,
                                            null,
                                            Collections.emptyList()
                                    );
                            accessor.setUser(userAuth);
                            Message<?> updatedMessage = MessageBuilder.withPayload(message.getPayload())
                                    .copyHeaders(accessor.toMap())
                                    .build();
                            return updatedMessage;
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            } else {
                System.out.println("[WS] SEND frame - Principal already set = " + (accessor.getUser() != null ? accessor.getUser().getName() : "null"));
            }
        }

        return message;
    }
}
