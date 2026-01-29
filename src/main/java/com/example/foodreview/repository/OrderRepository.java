package com.example.foodreview.repository;

import com.example.foodreview.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // =================================================================
    // 1. CHO KHÁCH HÀNG (USER)
    // =================================================================
    
    // A. Tìm theo Username (Dùng cho hàm getMyOrders ở bước trước)
    // JPA sẽ đi vào object 'User', tìm trường 'Username'
    List<Order> findByUser_UsernameOrderByCreatedAtDesc(String username);

    // B. Tìm theo User ID (Nếu bạn muốn dùng ID thay vì Username)
    // Lưu ý: Phải có dấu gạch dưới "_" (findByUser_Id) để JPA hiểu là tìm ID bên trong User
   


    // =================================================================
    // 2. CHO TÀI XẾ (DRIVER)
    // =================================================================

    // A. Tìm đơn chưa có tài xế (Để tài xế nhận đơn)
    // status thường là "CONFIRMED" (Quán đã làm xong) và driver là null
    List<Order> findByStatusAndDriverIsNullOrderByCreatedAtDesc(String status);

    // B. Tìm đơn tài xế ĐANG GIAO (Để hiện lên Dashboard tài xế)
    // Sửa 'findByDriverId' -> 'findByDriver_Id'
    List<Order> findByDriver_IdAndStatus(Long driverId, String status);
    
    // C. Lịch sử đơn của tài xế (Đã hoàn thành)
    // Sửa 'findByDriverId' -> 'findByDriver_Id'
    List<Order> findByDriver_IdOrderByCreatedAtDesc(Long driverId);


    // =================================================================
    // 3. CHO ADMIN
    // =================================================================
    
    // Lấy tất cả, mới nhất lên đầu
    List<Order> findAllByOrderByCreatedAtDesc();
    List<Order> findByUser_IdOrderByCreatedAtDesc(Long userId);
}