package com.example.foodreview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// Nếu bạn dùng bản Spring Boot mới, thêm dòng này để tắt warning JSON lúc nãy
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
// Cấu hình fix lỗi warning PageImpl (tùy chọn, nếu bạn muốn console sạch đẹp)
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class FoodreviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoodreviewApplication.class, args);
    }
}