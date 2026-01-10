package com.example.foodreview.service;

import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.example.foodreview.repository.FoodRepository;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FoodLookupTool {
    private final FoodRepository foodRepository;

    @Tool("Tìm kiếm món ăn trong database theo tên hoặc mô tả")
    public String searchFoods(String query) {
        var foods = foodRepository.searchFoodForBot(query, PageRequest.of(0, 5));
        if (foods.isEmpty()) return "Không tìm thấy món nào khớp với: " + query;
        return foods.stream()
                .map(f -> String.format("Món: %s | Giá: %,.0f VNĐ | Mô tả: %s", 
                        f.getName(), f.getPrice(), f.getDescription()))
                .collect(Collectors.joining("\n---\n"));
    }
}