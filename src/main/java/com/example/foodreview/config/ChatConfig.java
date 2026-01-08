package com.example.foodreview.config;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        // AI sẽ nhớ tối đa 10 tin nhắn gần nhất cho mỗi phiên chat
        return chatId -> MessageWindowChatMemory.withMaxMessages(10);
    }
}