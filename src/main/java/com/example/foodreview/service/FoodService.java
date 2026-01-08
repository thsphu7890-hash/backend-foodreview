package com.example.foodreview.service;

import com.example.foodreview.dto.FoodDTO;
import com.example.foodreview.model.Category;
import com.example.foodreview.model.Food;
import com.example.foodreview.model.Restaurant;
import com.example.foodreview.repository.CategoryRepository;
import com.example.foodreview.repository.FoodRepository;
import com.example.foodreview.repository.RestaurantRepository;
import com.example.foodreview.mapper.FoodMapper;
import com.example.foodreview.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet; // Import Set
import java.util.List;    // Import List

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepo;
    private final RestaurantRepository restaurantRepo;
    private final CategoryRepository categoryRepo;
    private final FoodMapper mapper;

    // 1. LẤY DANH SÁCH (Đã cập nhật tên hàm Repository)
    @Transactional(readOnly = true)
    public Page<FoodDTO> getAllFoods(String search, Long categoryId, Pageable pageable) {
        Page<Food> pageResult;

        if (categoryId != null && search != null && !search.isEmpty()) {
            // Sửa: findByCategory_Id -> findByCategories_Id
            pageResult = foodRepo.findByCategories_IdAndNameContainingIgnoreCase(categoryId, search, pageable);
        } else if (categoryId != null) {
            // Sửa: findByCategory_Id -> findByCategories_Id
            pageResult = foodRepo.findByCategories_Id(categoryId, pageable);
        } else if (search != null && !search.isEmpty()) {
            pageResult = foodRepo.findByNameContainingIgnoreCase(search, pageable);
        } else {
            pageResult = foodRepo.findAll(pageable);
        }

        return pageResult.map(mapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<FoodDTO> getByRestaurant(Long restaurantId, Pageable pageable) {
        return foodRepo.findByRestaurant_Id(restaurantId, pageable).map(mapper::toDTO);
    }
    
    @Transactional(readOnly = true)
    public FoodDTO getById(Long id) {
        Food food = foodRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy món ăn: " + id));
        return mapper.toDTO(food);
    }

    // --- 2. TẠO MỚI (Xử lý nhiều danh mục) ---
    @Transactional
    public FoodDTO create(FoodDTO dto) {
        Restaurant restaurant = restaurantRepo.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Nhà hàng không tồn tại"));
        
        // Mapper chuyển đổi cơ bản (Tên, giá, mô tả, ảnh, video...)
        Food food = mapper.toEntity(dto);
        food.setRestaurant(restaurant);

        // 👇 XỬ LÝ LƯU DANH SÁCH CATEGORY 👇
        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepo.findAllById(dto.getCategoryIds());
            food.setCategories(new HashSet<>(categories));
        }
        // -----------------------------------

        return mapper.toDTO(foodRepo.save(food));
    }

    // --- 3. CẬP NHẬT (Xử lý nhiều danh mục) ---
    @Transactional
    public FoodDTO update(Long id, FoodDTO dto) {
        Food food = foodRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy món ăn"));
        
        if (dto.getName() != null) food.setName(dto.getName());
        if (dto.getPrice() != null) food.setPrice(dto.getPrice());
        if (dto.getImage() != null) food.setImage(dto.getImage());
        if (dto.getDescription() != null) food.setDescription(dto.getDescription());
        if (dto.getVideo() != null) food.setVideo(dto.getVideo());

        // Cập nhật nhà hàng nếu có thay đổi
        if (dto.getRestaurantId() != null) {
             Restaurant r = restaurantRepo.findById(dto.getRestaurantId())
                     .orElseThrow(() -> new ResourceNotFoundException("Nhà hàng không tồn tại"));
             food.setRestaurant(r);
        }

        // 👇 XỬ LÝ CẬP NHẬT DANH SÁCH CATEGORY 👇
        if (dto.getCategoryIds() != null) {
            // Tìm tất cả category theo list ID mới
            List<Category> categories = categoryRepo.findAllById(dto.getCategoryIds());
            // Thay thế hoàn toàn danh sách cũ bằng danh sách mới
            food.setCategories(new HashSet<>(categories));
        }
        // ----------------------------------------

        return mapper.toDTO(foodRepo.save(food));
    }

    @Transactional
    public void delete(Long id) {
        if (!foodRepo.existsById(id)) throw new ResourceNotFoundException("Không tìm thấy món ăn");
        foodRepo.deleteById(id);
    }
}