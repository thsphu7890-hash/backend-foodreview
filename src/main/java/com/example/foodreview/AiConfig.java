package com.example.foodreview;

import com.example.foodreview.service.FoodAssistant;
import com.example.foodreview.service.FoodBotService;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value; // 1. Phải có import này
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    // 2. Đọc Key từ file properties
    @Value("${gemini.api-key}")
    private String apiKey;

    // 3. Đọc Model Name từ file properties
    @Value("${gemini.model-name}")
    private String modelName;

    @Bean
    ChatLanguageModel chatLanguageModel() {
        return GoogleAiGeminiChatModel.builder()
                .apiKey(apiKey)       // Sử dụng biến vừa đọc
                .modelName(modelName) // Sử dụng biến vừa đọc
                .build();
    }

    @Bean
    FoodAssistant foodAssistant(ChatLanguageModel chatLanguageModel, FoodBotService foodTools) {
        return AiServices.builder(FoodAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .tools(foodTools)
                .build();
    }
}