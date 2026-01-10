package com.example.foodreview.repository;

import com.example.foodreview.model.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    // 1. Tìm thông tin tài xế dựa trên User ID
    Optional<Driver> findByUserId(Long userId);

    // 2. Kiểm tra xem User này đã đăng ký chưa
    boolean existsByUserId(Long userId);

    // 👇 3. QUAN TRỌNG: Thêm hàm này để Service kiểm tra trùng SĐT khi đăng ký
    boolean existsByPhone(String phone);

    // 4. Tìm tài xế theo trạng thái (VD: PENDING để duyệt)
    Page<Driver> findByStatus(String status, Pageable pageable);

    // 5. Tìm tài xế theo SĐT (Để đăng nhập)
    Optional<Driver> findByPhone(String phone);

    // 6. (Nâng cao) Tìm tài xế đang Rảnh (ACTIVE) để giao đơn
    @Query("SELECT d FROM Driver d WHERE d.status = 'ACTIVE'")
    Page<Driver> findAvailableDrivers(Pageable pageable);
}