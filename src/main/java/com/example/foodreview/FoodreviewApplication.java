package com.example.foodreview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// Xóa dòng import EnableJpaRepositories nếu có

@SpringBootApplication
// 👇 XÓA BỎ DÒNG @EnableJpaRepositories(...) cũ đi. 
// Spring Boot sẽ tự động tìm thấy Repository nếu chúng nằm cùng project.
public class FoodreviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodreviewApplication.class, args);
    }
}