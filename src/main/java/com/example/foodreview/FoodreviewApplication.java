package com.example.foodreview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// 1. Thêm dòng import này:
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
// 2. Thêm dòng cấu hình này để sửa lỗi Warning:
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class FoodreviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodreviewApplication.class, args);
    }

}