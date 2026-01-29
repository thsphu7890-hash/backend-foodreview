package com.example.foodreview.mapper;

import com.example.foodreview.dto.OrderDTO;
import com.example.foodreview.dto.OrderItemDTO;
import com.example.foodreview.model.Order;
import com.example.foodreview.model.OrderItem;
import jakarta.persistence.EntityNotFoundException; // Cần import cái này
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    // 1. Entity -> DTO (Hiển thị ra giao diện)
    public OrderDTO toDTO(Order order) {
        if (order == null) return null;

        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        // Kiểm tra user an toàn
        dto.setUserId(order.getUser() != null ? order.getUser().getId() : null);
        
        dto.setCustomerName(order.getCustomerName());
        dto.setPhone(order.getPhone());
        dto.setAddress(order.getAddress());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setCreatedAt(order.getCreatedAt());

        // --- SỬA LỖI Ở ĐÂY ---
        // Bọc trong try-catch để xử lý trường hợp ID tài xế trong bảng Order là 3
        // nhưng trong bảng Driver không có ID 3 (Lỗi Data Inconsistency)
        try {
            if (order.getDriver() != null) {
                // Lấy ID thì không sao (vì nó nằm sẵn trong bảng Order)
                dto.setDriverId(order.getDriver().getId());
                
                // Nhưng khi .getFullName() -> Hibernate mới quét vào bảng Driver -> Gây lỗi nếu không thấy
                dto.setDriverName(order.getDriver().getFullName());
                dto.setDriverPhone(order.getDriver().getPhone());
            }
        } catch (EntityNotFoundException e) {
            // Nếu tìm không thấy tài xế, set giá trị mặc định để API không bị chết
            System.err.println("CẢNH BÁO: Dữ liệu đơn hàng " + order.getId() + " tham chiếu đến Driver không tồn tại!");
            dto.setDriverName("Tài xế không tồn tại");
            dto.setDriverPhone("");
        }
        // ---------------------

        // Map Items (Danh sách món ăn)
        if (order.getItems() != null) {
            List<OrderItemDTO> itemDTOs = order.getItems().stream()
                    .map(this::toItemDTO) 
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

        // Map thông tin món ăn (Cũng nên kiểm tra null cho an toàn)
        if (item.getFood() != null) {
            dto.setFoodId(item.getFood().getId());
            dto.setFoodName(item.getFood().getName());
            dto.setFoodImage(item.getFood().getImage());
        } else {
            dto.setFoodName("Món ăn đã bị xóa");
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