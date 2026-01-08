package com.example.foodreview.repository;

import com.example.foodreview.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // 1. Cho Khách Hàng (Tìm theo User ID của người đặt)
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 2. Cho Tài Xế (DRIVER)
    
    // A. Tìm đơn chưa có tài xế (driver là null)
    // JPA tự hiểu driver là một object, IsNull kiểm tra khóa ngoại
    List<Order> findByStatusAndDriverIsNullOrderByCreatedAtDesc(String status);

    // B. Tìm đơn tài xế đang giao
    // JPA sẽ tự động hiểu "findByDriverId" là tìm theo trường "id" bên trong object "Driver"
    List<Order> findByDriverIdAndStatus(Long driverId, String status);
    
    // C. Lịch sử đơn của tài xế
    List<Order> findByDriverIdOrderByCreatedAtDesc(Long driverId);
    
    // 3. Cho Admin
    List<Order> findAllByOrderByCreatedAtDesc();
  
}