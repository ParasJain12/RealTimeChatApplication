package com.realtime.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	private final WebSocketAuthInterceptor authInterceptor;

	public WebSocketConfig(WebSocketAuthInterceptor authInterceptor) {
		super();
		this.authInterceptor = authInterceptor;
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// Client will connect to /ws endpoint and sockJS fallback enabled
		registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// Prefix for messages from SERVER to CLIENT
		registry.enableSimpleBroker("/topic","/queue");
		
		// Prefix for messages from CLIENT TO SERVER
		registry.setApplicationDestinationPrefixes("/app");
		
		// User destination prefix for one to one messaging
		registry.setUserDestinationPrefix("/user");
	}
	
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(authInterceptor);
	}
	
}
