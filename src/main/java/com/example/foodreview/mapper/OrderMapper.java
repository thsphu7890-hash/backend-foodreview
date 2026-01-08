package com.example.foodreview.mapper;

import com.example.foodreview.dto.OrderDTO;
import com.example.foodreview.dto.OrderItemDTO;
import com.example.foodreview.model.Order;
import com.example.foodreview.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    // 1. Entity -> DTO (Gửi dữ liệu ra Frontend)
    public OrderDTO toDTO(Order order) {
        if (order == null) return null;

        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        
        // Map thông tin User (Khách hàng)
        if (order.getUser() != null) {
            dto.setUserId(order.getUser().getId());
        }
        
        dto.setCustomerName(order.getCustomerName());
        dto.setPhone(order.getPhone());
        dto.setAddress(order.getAddress());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());

        // --- 👇 MAP TÀI XẾ (Đã bổ sung Avatar) 👇 ---
        if (order.getDriver() != null) {
            dto.setDriverId(order.getDriver().getId());
            dto.setDriverName(order.getDriver().getFullName()); // Lấy tên từ Driver, không phải User
            dto.setDriverPhone(order.getDriver().getPhone());
            dto.setDriverPlate(order.getDriver().getLicensePlate());
            
            // ✅ THÊM DÒNG NÀY: Để hiện ảnh tài xế trên Frontend
            dto.setDriverAvatar(order.getDriver().getAvatar()); 
        }
        // -----------------------------------------

        // Map danh sách món ăn
        if (order.getItems() != null) {
            dto.setItems(order.getItems().stream().map(this::toOrderItemDTO).collect(Collectors.toList()));
        }

        return dto;
    }

    // 2. DTO -> Entity (Nhận dữ liệu từ Frontend tạo đơn)
    public Order toEntity(OrderDTO dto) {
        if (dto == null) return null;
        
        Order order = new Order();
        // Lưu ý: User và Driver sẽ được set trong Service
        order.setCustomerName(dto.getCustomerName());
        order.setPhone(dto.getPhone());
        order.setAddress(dto.getAddress());
        order.setTotalAmount(dto.getTotalAmount());
        order.setPaymentMethod(dto.getPaymentMethod());
        
        return order;
    }
    
    // Helper map Item
    private OrderItemDTO toOrderItemDTO(OrderItem item) {
        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setFoodId(item.getFood().getId());
        itemDTO.setFoodName(item.getFood().getName());
        
        // ✅ SỬA LẠI TÊN HÀM: Trong DTO thường đặt là 'setImage', 
        // nếu DTO của bạn là 'setFoodImage' thì giữ nguyên, nhưng Frontend đang dùng 'item.image'
        itemDTO.setImage(item.getFood().getImage()); 
        
        itemDTO.setQuantity(item.getQuantity());
        itemDTO.setPrice(item.getPrice());
        return itemDTO;
    }
}