package com.example.foodreview.service;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component; // Bắt buộc có dòng này

@Component("foodBotService") // <-- PHẢI CÓ DÒNG NÀY thì mới hết lỗi
public class FoodBotService {
    
    @Tool("Tìm kiếm món ăn...")
    public String searchFoodFromDatabase(String query) {
        return "Kết quả tìm kiếm: " + query;
    }
}