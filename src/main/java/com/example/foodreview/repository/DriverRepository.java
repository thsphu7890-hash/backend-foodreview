package com.example.foodreview.repository;

import com.example.foodreview.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    boolean existsByPhone(String phone);
    
    // 👇 Bắt buộc phải có hàm này để tìm tài xế đăng nhập
    Optional<Driver> findByPhone(String phone);
}