package com.example.foodreview.service;

import com.example.foodreview.dto.OrderDTO;
import com.example.foodreview.dto.OrderItemDTO;
import com.example.foodreview.model.Food;
import com.example.foodreview.model.Order;
import com.example.foodreview.model.OrderItem;
import com.example.foodreview.model.User;
import com.example.foodreview.mapper.OrderMapper;
import com.example.foodreview.repository.OrderRepository;
import com.example.foodreview.repository.FoodRepository;
import com.example.foodreview.repository.UserRepository;
import com.example.foodreview.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    /**
     * 1. TẠO ĐƠN HÀNG MỚI
     */
    @Transactional
    public OrderDTO createOrder(OrderDTO dto) {
        Order order = orderMapper.toEntity(dto);
        order.setStatus("PENDING");

        if (dto.getUserId() != null) {
            User user = userRepo.findById(dto.getUserId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Tài khoản không tồn tại!"));
            order.setUser(user);
            
            if (order.getCustomerName() == null) order.setCustomerName(user.getFullName());
            if (order.getPhone() == null) order.setPhone(user.getPhone());
            if (order.getAddress() == null) order.setAddress(user.getAddress());
        }

        List<OrderItem> orderItems = new ArrayList<>();
        double calculatedTotal = 0;

        for (OrderItemDTO itemDto : dto.getItems()) {
            Food food = foodRepo.findById(itemDto.getFoodId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, 
                            "Món ăn (ID: " + itemDto.getFoodId() + ") không còn tồn tại!"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setFood(food);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPrice(food.getPrice()); 

            orderItems.add(orderItem);
            calculatedTotal += orderItem.getPrice() * orderItem.getQuantity();
        }

        order.setItems(orderItems);
        order.setTotalAmount(calculatedTotal);
        
        Order savedOrder = orderRepo.save(order);
        return orderMapper.toDTO(savedOrder);
    }

    /**
     * 2. LẤY DANH SÁCH ĐƠN HÀNG CỦA 1 USER
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByUser(Long userId) {
        return orderRepo.findByUserId(userId)
                .stream()
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt())) 
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 3. HỦY ĐƠN HÀNG
     */
    @Transactional
    public OrderDTO cancelOrder(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng ID: " + orderId));

        if ("PENDING".equals(order.getStatus()) || "CONFIRMED".equals(order.getStatus())) {
            order.setStatus("CANCELLED");
            return orderMapper.toDTO(orderRepo.save(order));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đơn hàng đang giao hoặc đã xong, không thể hủy!");
        }
    }

    /**
     * 4. CẬP NHẬT TRẠNG THÁI (Cho Admin/Driver)
     */
    @Transactional
    public OrderDTO updateStatus(Long id, String status) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng ID: " + id));
        
        order.setStatus(status);
        return orderMapper.toDTO(orderRepo.save(order));
    }
    
    /**
     * 5. LẤY TẤT CẢ ĐƠN HÀNG
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        return orderRepo.findAll().stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 6. ADMIN GÁN TÀI XẾ CHO ĐƠN HÀNG (ĐÃ SỬA LỖI)
     */
    @Transactional
    public OrderDTO assignDriver(Long orderId, Long driverId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng ID: " + orderId));

        User driver = userRepo.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài xế ID: " + driverId));

        // ✅ SỬA LỖI 1: Dùng setDriver thay vì setShipperId
        order.setDriver(driver);

        // ✅ SỬA LỖI 2: Viết chuẩn cú pháp, xóa chữ "status:" thừa
        order.setStatus("SHIPPING");

        return orderMapper.toDTO(orderRepo.save(order));
    }
}