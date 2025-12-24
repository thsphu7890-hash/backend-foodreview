package com.example.foodreview.repository;

import com.example.foodreview.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // 1. Tìm đơn hàng theo User ID (Lịch sử khách đặt)
    List<Order> findByUserId(Long userId);

    // 2. Tìm đơn hàng theo Tên khách (Code cũ của bạn, giữ lại để không lỗi chỗ khác nếu cần)
    List<Order> findByCustomerNameOrderByCreatedAtDesc(String customerName);

    // --- 👇 CÁC HÀM MỚI CHO TÀI XẾ (THÊM VÀO ĐỂ HẾT LỖI ẢNH 1) 👇 ---

    // Tìm đơn "Kèo thơm": Có status cụ thể và CHƯA CÓ tài xế
    List<Order> findByStatusAndDriverIsNull(String status);

    // Tìm đơn đang giao của tài xế cụ thể
    List<Order> findByDriverIdAndStatus(Long driverId, String status);
    
    // Tìm lịch sử đơn của tài xế
    List<Order> findByDriverId(Long driverId);
}