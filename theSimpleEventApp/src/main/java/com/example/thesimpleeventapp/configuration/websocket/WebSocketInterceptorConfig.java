package com.example.thesimpleeventapp.configuration.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketInterceptorConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketInterceptorConfig.class);
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                logMessage("Inbound", message);
                return message;
            }
        });
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                logMessage("Outbound", message);
                return message;
            }
        });
    }

    private void logMessage(String direction, Message<?> message) {
        try {
            String payloadJson = objectMapper.writeValueAsString(message.getPayload());
            logger.info("[{}] Payload: {}", direction, payloadJson);
            logger.debug("[{}] Headers: {}", direction, message.getHeaders());
        } catch (Exception e) {
            logger.warn("[{}] Failed to serialize payload: {}", direction, e.getMessage());
        }
    }
}