package com.example.foodreview.service;

import com.example.foodreview.dto.OrderDTO;
import com.example.foodreview.dto.OrderItemDTO;
import com.example.foodreview.mapper.OrderMapper;
import com.example.foodreview.model.*;
import com.example.foodreview.repository.*;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;
    private final FoodRepository foodRepo;
    private final UserRepository userRepo;
    private final OrderMapper orderMapper;

    // 1. TẠO ĐƠN
    @Transactional
    public OrderDTO createOrder(OrderDTO dto) {
        Order order = orderMapper.toEntity(dto);
        order.setStatus("PENDING");

        if (dto.getUserId() != null) {
            User user = userRepo.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            order.setUser(user);
            if (order.getCustomerName() == null) order.setCustomerName(user.getFullName());
            if (order.getPhone() == null) order.setPhone(user.getPhone());
            if (order.getAddress() == null) order.setAddress(user.getAddress());
        }

        List<OrderItem> orderItems = new ArrayList<>();
        double calculatedTotal = 0;
        if (dto.getItems() != null) {
            for (OrderItemDTO itemDto : dto.getItems()) {
                Food food = foodRepo.findById(itemDto.getFoodId())
                        .orElseThrow(() -> new RuntimeException("Món ăn không tồn tại"));
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setFood(food);
                orderItem.setQuantity(itemDto.getQuantity());
                orderItem.setPrice(food.getPrice());
                orderItems.add(orderItem);
                calculatedTotal += orderItem.getPrice() * orderItem.getQuantity();
            }
        }
        order.setItems(orderItems);
        order.setTotalAmount(calculatedTotal);
        return orderMapper.toDTO(orderRepo.save(order));
    }

    // 2. LẤY ĐƠN HÀNG CỦA CHÍNH TÔI (Cho User Dashboard)
    @Transactional(readOnly = true)
    public List<OrderDTO> getMyOrders() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return orderRepo.findByUser_UsernameOrderByCreatedAtDesc(currentUsername)
                .stream().map(orderMapper::toDTO).collect(Collectors.toList());
    }

    // 3. LẤY CHI TIẾT
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        return orderMapper.toDTO(order);
    }
    
    // 4. LẤY TẤT CẢ (Cho Admin)
    public List<OrderDTO> getAllOrders() {
        return orderRepo.findAllByOrderByCreatedAtDesc().stream()
                .map(orderMapper::toDTO).collect(Collectors.toList());
    }
    
    // 5. HỦY ĐƠN
    public OrderDTO cancelOrder(Long id) {
        Order order = orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        order.setStatus("CANCELLED");
        return orderMapper.toDTO(orderRepo.save(order));
    }
    
    // 6. LẤY THEO USER ID (Admin xem lịch sử user)
    public List<OrderDTO> getOrdersByUser(Long userId) {
        return orderRepo.findByUser_IdOrderByCreatedAtDesc(userId)
                .stream().map(orderMapper::toDTO).collect(Collectors.toList());
    }
}