package com.example.foodreview.mapper;

import com.example.foodreview.dto.OrderDTO;
import com.example.foodreview.dto.OrderItemDTO;
import com.example.foodreview.model.Food;
import com.example.foodreview.model.Order;
import com.example.foodreview.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    // Chuyển từ Entity (Dữ liệu DB) sang DTO (Dữ liệu trả về API)
    public OrderDTO toDTO(Order order) {
        if (order == null) return null;

        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        
        // 1. Map thông tin cơ bản
        dto.setCustomerName(order.getCustomerName());
        dto.setPhone(order.getPhone());
        dto.setAddress(order.getAddress());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        
        // 2. Map ID người dùng (User)
        if (order.getUser() != null) {
            dto.setUserId(order.getUser().getId());
        }

        // 3. Map thông tin Tài xế (Driver) - QUAN TRỌNG
        // Kiểm tra xem đơn đã có tài xế nhận chưa
        if (order.getDriver() != null) {
            dto.setDriverId(order.getDriver().getId());
            dto.setDriverName(order.getDriver().getFullName()); // Lấy tên thật tài xế
            dto.setDriverPhone(order.getDriver().getPhone());   // Lấy SĐT tài xế
        }

        // 4. Map danh sách món ăn
        if (order.getItems() != null) {
            List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());
            dto.setItems(itemDTOs);

            // 5. LOGIC LẤY THÔNG TIN QUÁN (RESTAURANT)
            // Lấy thông tin quán từ món ăn đầu tiên trong đơn hàng
            if (!order.getItems().isEmpty()) {
                Food firstFood = order.getItems().get(0).getFood();
                if (firstFood != null && firstFood.getRestaurant() != null) {
                    dto.setRestaurantName(firstFood.getRestaurant().getName());
                    dto.setRestaurantAddress(firstFood.getRestaurant().getAddress());
                }
            }
        }

        return dto;
    }

    // Chuyển từ DTO sang Entity (Khi tạo đơn mới)
    public Order toEntity(OrderDTO dto) {
        if (dto == null) return null;

        Order order = new Order();
        // ID tự sinh, không set ID ở đây
        order.setCustomerName(dto.getCustomerName());
        order.setPhone(dto.getPhone());
        order.setAddress(dto.getAddress());
        order.setTotalAmount(dto.getTotalAmount());
        order.setPaymentMethod(dto.getPaymentMethod());
        order.setStatus(dto.getStatus());
        
        // Lưu ý: User và Driver sẽ được set trong OrderService, không set ở đây
        return order;
    }

    // Helper: Map từng món ăn
    private OrderItemDTO toItemDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        
        if (item.getFood() != null) {
            dto.setFoodId(item.getFood().getId());
            dto.setFoodName(item.getFood().getName());
            dto.setFoodImage(item.getFood().getImage());
        }
        return dto;
    }
}