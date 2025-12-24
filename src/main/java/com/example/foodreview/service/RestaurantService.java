package com.example.foodreview.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.foodreview.model.Restaurant;
import com.example.foodreview.model.Category;
import com.example.foodreview.dto.RestaurantDTO;
import com.example.foodreview.mapper.RestaurantMapper;
import com.example.foodreview.repository.CategoryRepository;
import com.example.foodreview.repository.RestaurantRepository;
// Nếu chưa có class này, bạn có thể dùng RuntimeException tạm
import com.example.foodreview.exception.ResourceNotFoundException; 

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Nên thêm cái này để đảm bảo toàn vẹn dữ liệu
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepo;
    private final CategoryRepository categoryRepo;
    private final RestaurantMapper mapper;

    // 1. Lấy tất cả
    public List<RestaurantDTO> getAll() {
        return restaurantRepo.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    // 2. Lấy theo ID (Thêm mới)
    public RestaurantDTO getById(Long id) {
        Restaurant r = restaurantRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
        return mapper.toDTO(r);
    }

    // 3. Lấy theo Category
    public List<RestaurantDTO> getByCategory(Long categoryId) {
        return restaurantRepo.findByCategoryId(categoryId)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    // 4. Tạo mới
    @Transactional
    public RestaurantDTO create(RestaurantDTO dto) {
        Category c = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Restaurant r = mapper.toEntity(dto);
        r.setCategory(c);

        return mapper.toDTO(restaurantRepo.save(r));
    }

    // 5. Cập nhật (Thêm mới - Quan trọng)
    @Transactional
    public RestaurantDTO update(Long id, RestaurantDTO dto) {
        Restaurant existing = restaurantRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));

        // Cập nhật thông tin cơ bản
        existing.setName(dto.getName());
        existing.setAddress(dto.getAddress());
        existing.setImage(dto.getImage());
        existing.setDescription(dto.getDescription()); // Đừng quên dòng này

        // Cập nhật Category nếu có thay đổi
        if (dto.getCategoryId() != null) {
            Category c = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            existing.setCategory(c);
        }

        return mapper.toDTO(restaurantRepo.save(existing));
    }

    // 6. Xóa (Thêm mới)
    @Transactional
    public void delete(Long id) {
        if (!restaurantRepo.existsById(id)) {
            throw new ResourceNotFoundException("Restaurant not found with id: " + id);
        }
        restaurantRepo.deleteById(id);
    }
}