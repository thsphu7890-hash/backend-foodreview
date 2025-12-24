package com.example.foodreview.service;

import com.example.foodreview.dto.OrderDTO;
import com.example.foodreview.mapper.OrderMapper;
import com.example.foodreview.model.Order;
import com.example.foodreview.model.User;
import com.example.foodreview.repository.OrderRepository;
import com.example.foodreview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final OrderRepository orderRepo;
    private final UserRepository userRepo;
    private final OrderMapper orderMapper;

    /**
     * 1. LẤY DANH SÁCH ĐƠN "CÓ SẴN" (Chưa ai nhận)
     * Điều kiện: Status = CONFIRMED (Quán đã làm xong) và Shipper = null
     */
    public List<OrderDTO> getAvailableOrders() {
        // Tìm các đơn đã xác nhận và chưa có tài xế
        List<Order> orders = orderRepo.findAll().stream()
                .filter(o -> "CONFIRMED".equals(o.getStatus()) && o.getDriver() == null)
                .collect(Collectors.toList());
        
        return orders.stream().map(orderMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * 2. TÀI XẾ BẤM "NHẬN ĐƠN"
     * Quan trọng: Phải dùng @Transactional để tránh 2 tài xế nhận cùng 1 đơn
     */
    @Transactional
    public void acceptOrder(Long orderId, Long driverId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại!"));

        // Kiểm tra kỹ lại lần nữa xem đơn đã có người nhận chưa
        if (order.getDriver() != null) {
            throw new RuntimeException("Rất tiếc! Đơn hàng này đã có tài xế khác nhận rồi.");
        }

        User driver = userRepo.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Tài xế không tồn tại"));

        // Gán tài xế và đổi trạng thái
        order.setDriver(driver);
        order.setStatus("SHIPPING"); // Chuyển sang đang giao
        orderRepo.save(order);
    }

    /**
     * 3. LẤY ĐƠN ĐANG GIAO CỦA TÀI XẾ (Chỉ 1 đơn tại 1 thời điểm)
     */
    public List<OrderDTO> getCurrentOrder(Long driverId) {
        return orderRepo.findAll().stream()
                .filter(o -> "SHIPPING".equals(o.getStatus()) 
                        && o.getDriver() != null 
                        && o.getDriver().getId().equals(driverId))
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 4. HOÀN THÀNH ĐƠN HÀNG (Giao xong)
     */
    @Transactional
    public void completeOrder(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));
        
        order.setStatus("DELIVERED"); // Hoàn thành
        orderRepo.save(order);
    }
    
    /**
     * 5. LỊCH SỬ GIAO HÀNG
     */
    public List<OrderDTO> getHistory(Long driverId) {
         return orderRepo.findAll().stream()
                .filter(o -> "DELIVERED".equals(o.getStatus()) 
                        && o.getDriver() != null 
                        && o.getDriver().getId().equals(driverId))
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }
} 