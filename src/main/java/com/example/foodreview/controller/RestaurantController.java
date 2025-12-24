package com.example.foodreview.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.example.foodreview.dto.RestaurantDTO;
import com.example.foodreview.mapper.RestaurantMapper;
import com.example.foodreview.model.Category;
import com.example.foodreview.model.Restaurant;
import com.example.foodreview.repository.CategoryRepository;
import com.example.foodreview.repository.RestaurantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin(origins = "http://localhost:5173") // Đảm bảo đúng cổng React
public class RestaurantController {

    @Autowired
    private RestaurantRepository restaurantRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    // Inject thêm Mapper
    @Autowired
    private RestaurantMapper restaurantMapper;

    // 1. Lấy tất cả (Trả về List DTO)
    @GetMapping
    public List<RestaurantDTO> getAll() {
        return restaurantRepo.findAll().stream()
                .map(restaurantMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 2. Lấy theo ID
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> getById(@PathVariable Long id) {
        Restaurant restaurant = restaurantRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Nhà hàng"));
        return ResponseEntity.ok(restaurantMapper.toDTO(restaurant));
    }

    // 3. Tạo mới (Nhận DTO -> Lưu Entity -> Trả DTO)
    @PostMapping
    public ResponseEntity<RestaurantDTO> create(@RequestBody RestaurantDTO dto) {
        // Chuyển DTO sang Entity
        Restaurant entity = restaurantMapper.toEntity(dto);

        // Xử lý Category (vì Mapper không gọi Repository)
        if (dto.getCategoryId() != null) {
            Category c = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category không tồn tại"));
            entity.setCategory(c);
        }

        Restaurant saved = restaurantRepo.save(entity);
        return ResponseEntity.ok(restaurantMapper.toDTO(saved));
    }

    // 4. Cập nhật (Nhận DTO, cập nhật cả mô tả)
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> update(@PathVariable Long id, @RequestBody RestaurantDTO dto) {
        Restaurant old = restaurantRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà hàng để sửa"));

        // Cập nhật các trường thông tin
        old.setName(dto.getName());
        old.setAddress(dto.getAddress());
        old.setImage(dto.getImage());
        
        // --- QUAN TRỌNG: Cập nhật mô tả ---
        old.setDescription(dto.getDescription());

        // Cập nhật Category nếu có thay đổi
        if (dto.getCategoryId() != null) {
            Category c = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category không tồn tại"));
            old.setCategory(c);
        }

        Restaurant saved = restaurantRepo.save(old);
        return ResponseEntity.ok(restaurantMapper.toDTO(saved));
    }

    // 5. Xóa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        restaurantRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    // 6. Lấy theo danh mục (Optional)
    @GetMapping("/category/{id}")
    public List<RestaurantDTO> getByCategory(@PathVariable Long id) {
        return restaurantRepo.findByCategoryId(id).stream()
                .map(restaurantMapper::toDTO)
                .collect(Collectors.toList());
    }
}