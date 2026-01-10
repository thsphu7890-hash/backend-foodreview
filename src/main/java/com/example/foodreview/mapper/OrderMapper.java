package com.example.foodreview.mapper;

import com.example.foodreview.dto.OrderDTO;
import com.example.foodreview.dto.OrderItemDTO;
import com.example.foodreview.model.Order;
import com.example.foodreview.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    // 1. Entity -> DTO (Hiển thị)
    public OrderDTO toDTO(Order order) {
        if (order == null) return null;

        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser() != null ? order.getUser().getId() : null);
        
        dto.setCustomerName(order.getCustomerName());
        dto.setPhone(order.getPhone());
        dto.setAddress(order.getAddress());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setCreatedAt(order.getCreatedAt());

        // Map Driver
        if (order.getDriver() != null) {
            dto.setDriverId(order.getDriver().getId());
            dto.setDriverName(order.getDriver().getFullName());
            dto.setDriverPhone(order.getDriver().getPhone());
        }

        // Map Items (Danh sách món ăn)
        if (order.getItems() != null) {
            List<OrderItemDTO> itemDTOs = order.getItems().stream()
                    .map(this::toItemDTO) // Gọi hàm helper bên dưới
                    .collect(Collectors.toList());
            dto.setItems(itemDTOs);
        }

        return dto;
    }

    // Helper: Map từng item con
    private OrderItemDTO toItemDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());

        // Lấy thông tin từ Food (Vì FetchType.LAZY nên cần cẩn thận, 
        // nhưng trong Service có @Transactional nên OK)
        if (item.getFood() != null) {
            dto.setFoodId(item.getFood().getId());
            dto.setFoodName(item.getFood().getName());
            dto.setFoodImage(item.getFood().getImage());
        }
        return dto;
    }

    // 2. DTO -> Entity (Tạo đơn)
    public Order toEntity(OrderDTO dto) {
        if (dto == null) return null;
        Order order = new Order();
        order.setCustomerName(dto.getCustomerName());
        order.setPhone(dto.getPhone());
        order.setAddress(dto.getAddress());
        order.setPaymentMethod(dto.getPaymentMethod());
        // Lưu ý: Items sẽ được xử lý riêng trong Service
        return order;
    }
}