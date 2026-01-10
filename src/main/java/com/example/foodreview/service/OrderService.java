package com.example.foodreview.service;

import com.example.foodreview.dto.OrderDTO;
import com.example.foodreview.dto.OrderItemDTO;
import com.example.foodreview.mapper.OrderMapper;
import com.example.foodreview.model.*;
import com.example.foodreview.repository.ReviewRepository;
import com.example.foodreview.repository.DriverRepository;
import com.example.foodreview.repository.FoodRepository;
import com.example.foodreview.repository.OrderRepository;
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
    private final DriverRepository driverRepo; 
    private final ReviewRepository reviewRepo; 
    private final OrderMapper orderMapper;

    /**
     * 1. TẠO ĐƠN HÀNG MỚI
     */
    @Transactional
    public OrderDTO createOrder(OrderDTO dto) {
        Order order = orderMapper.toEntity(dto);
        order.setStatus("PENDING");

        // Gán User
        if (dto.getUserId() != null) {
            User user = userRepo.findById(dto.getUserId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User không tồn tại"));
            order.setUser(user);
            
            // Snapshot thông tin giao hàng nếu chưa có
            if (order.getCustomerName() == null) order.setCustomerName(user.getFullName());
            if (order.getPhone() == null) order.setPhone(user.getPhone());
            if (order.getAddress() == null) order.setAddress(user.getAddress());
        }

        // Xử lý món ăn và tính tiền
        List<OrderItem> orderItems = new ArrayList<>();
        double calculatedTotal = 0;

        if (dto.getItems() != null) {
            for (OrderItemDTO itemDto : dto.getItems()) {
                Food food = foodRepo.findById(itemDto.getFoodId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Món ăn không tồn tại: " + itemDto.getFoodId()));

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
        
        Order savedOrder = orderRepo.save(order);
        return orderMapper.toDTO(savedOrder);
    }

    /**
     * 2. LẤY DANH SÁCH ĐƠN HÀNG CỦA USER (Lịch sử)
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByUser(Long userId) {
        List<Order> orders = orderRepo.findByUserIdOrderByCreatedAtDesc(userId);

        return orders.stream().map(order -> {
            OrderDTO dto = orderMapper.toDTO(order);
            checkIsReviewed(order.getId(), dto); // Kiểm tra review
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 3. LẤY CHI TIẾT ĐƠN HÀNG (MỚI THÊM)
     * Dùng cho trang OrderDetail
     */
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với ID: " + orderId));

        OrderDTO dto = orderMapper.toDTO(order);
        
        // Kiểm tra từng món đã được đánh giá chưa để hiển thị nút
        checkIsReviewed(order.getId(), dto);

        return dto;
    }

    /**
     * 4. HỦY ĐƠN HÀNG
     */
    @Transactional
    public OrderDTO cancelOrder(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));

        if ("PENDING".equals(order.getStatus()) || "CONFIRMED".equals(order.getStatus())) {
            order.setStatus("CANCELLED");
            return orderMapper.toDTO(orderRepo.save(order));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đơn hàng đang giao hoặc đã hoàn thành, không thể hủy!");
        }
    }

    /**
     * 5. CẬP NHẬT TRẠNG THÁI (Admin/Driver)
     */
    @Transactional
    public OrderDTO updateStatus(Long id, String status) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng"));
        
        order.setStatus(status);
        return orderMapper.toDTO(orderRepo.save(order));
    }
    
    /**
     * 6. LẤY TẤT CẢ ĐƠN HÀNG (Admin)
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        return orderRepo.findAll().stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 7. GÁN TÀI XẾ
     */
    @Transactional
    public OrderDTO assignDriver(Long orderId, Long driverId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Đơn hàng không tồn tại"));

        Driver driver = driverRepo.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Tài xế không tồn tại"));

        if (order.getDriver() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đơn hàng này đã có tài xế nhận!");
        }

        order.setDriver(driver);
        order.setStatus("DELIVERING"); 

        return orderMapper.toDTO(orderRepo.save(order));
    }

    // --- Helper Method: Kiểm tra trạng thái đánh giá ---
    private void checkIsReviewed(Long orderId, OrderDTO dto) {
        if (dto.getItems() != null) {
            dto.getItems().forEach(itemDto -> {
                boolean isReviewed = reviewRepo.existsByOrderIdAndFoodId(orderId, itemDto.getFoodId());
                itemDto.setIsReviewed(isReviewed);
            });
        }
    }
}