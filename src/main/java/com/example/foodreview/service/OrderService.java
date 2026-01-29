package com.example.foodreview.service;

// 1. IMPORT DTO
import com.example.foodreview.dto.OrderDTO;
import com.example.foodreview.dto.OrderItemDTO;

// 2. IMPORT MAPPER & EXCEPTION
import com.example.foodreview.mapper.OrderMapper;
import com.example.foodreview.exception.ResourceNotFoundException;

// 3. IMPORT MODEL & REPOSITORY
import com.example.foodreview.model.*;
import com.example.foodreview.repository.*;

// 4. IMPORT THƯ VIỆN SPRING
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    
    // Inject Mapper
    private final OrderMapper orderMapper; 

    // --- 1. TẠO ĐƠN HÀNG ---
    @Transactional
    public OrderDTO createOrder(OrderDTO orderRequest) {
        // A. Validate giỏ hàng
        if (orderRequest.getItems() == null || orderRequest.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giỏ hàng trống! Vui lòng chọn món.");
        }

        // B. Validate User
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại ID: " + orderRequest.getUserId()));

        // C. Tạo Order Entity
        Order order = new Order();
        order.setUser(user);
        
        // Lấy thông tin người nhận (Ưu tiên từ Request gửi lên, nếu không có thì lấy từ Profile User)
        // Lưu ý: Đảm bảo User entity của bạn có các hàm getFullName, getPhone, getAddress
        order.setCustomerName(orderRequest.getCustomerName() != null && !orderRequest.getCustomerName().isEmpty() 
                ? orderRequest.getCustomerName() 
                : (user.getFullName() != null ? user.getFullName() : user.getUsername()));
                
        order.setPhone(orderRequest.getPhone() != null && !orderRequest.getPhone().isEmpty()
                ? orderRequest.getPhone() 
                : (user.getPhone() != null ? user.getPhone() : ""));
                
        order.setAddress(orderRequest.getAddress() != null && !orderRequest.getAddress().isEmpty()
                ? orderRequest.getAddress() 
                : (user.getAddress() != null ? user.getAddress() : ""));
        
        order.setStatus("PENDING");
        order.setPaymentMethod(orderRequest.getPaymentMethod() != null ? orderRequest.getPaymentMethod() : "COD");

        // D. Xử lý danh sách món ăn
        List<OrderItem> entityItems = new ArrayList<>();
        double totalAmount = 0;

        for (OrderItemDTO itemDTO : orderRequest.getItems()) {
            Food food = foodRepository.findById(itemDTO.getFoodId())
                    .orElseThrow(() -> new ResourceNotFoundException("Món ăn không tồn tại ID: " + itemDTO.getFoodId()));

            OrderItem item = new OrderItem();
            item.setOrder(order); // Quan trọng: set cha cho con
            item.setFood(food);
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(food.getPrice()); // Lấy giá từ DB để bảo mật

            totalAmount += (food.getPrice() * itemDTO.getQuantity());
            entityItems.add(item);
        }

        order.setItems(entityItems);
        order.setTotalAmount(totalAmount);

        // E. Lưu vào DB và trả về DTO
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDTO(savedOrder);
    }

    // --- 2. LẤY DANH SÁCH ĐƠN HÀNG CỦA TÔI ---
    @Transactional(readOnly = true)
    public List<OrderDTO> getMyOrders() {
        // 1. Lấy username từ Token hiện tại
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        
        // 2. Gọi Repository tìm theo Username
        // Đảm bảo trong OrderRepository đã có hàm: findByUser_UsernameOrderByCreatedAtDesc
        List<Order> orders = orderRepository.findByUser_UsernameOrderByCreatedAtDesc(username);

        // 3. Convert sang DTO
        return orders.stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    // --- 3. LẤY TẤT CẢ (ADMIN) ---
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        // Đảm bảo Repository có hàm: findAllByOrderByCreatedAtDesc
        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    // --- 4. LẤY CHI TIẾT ĐƠN HÀNG ---
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng ID: " + id));
        return orderMapper.toDTO(order);
    }
    
    // --- 5. HỦY ĐƠN HÀNG ---
    @Transactional
    public OrderDTO cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng ID: " + id));
        
        // Chỉ cho phép hủy khi đơn mới tạo (PENDING) hoặc đã xác nhận (CONFIRMED)
        if ("PENDING".equals(order.getStatus()) || "CONFIRMED".equals(order.getStatus())) {
             order.setStatus("CANCELLED");
             return orderMapper.toDTO(orderRepository.save(order));
        } else {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đơn hàng đang giao hoặc đã hoàn thành, không thể hủy!");
        }
    }
}