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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepo;
    private final RestaurantRepository restaurantRepo;
    private final CategoryRepository categoryRepo;
    private final FoodMapper mapper;

    // --- 1. ADMIN: Lấy tất cả (Phân trang) ---
    @Transactional(readOnly = true)
    public Page<FoodDTO> getAllFoods(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            return foodRepo.findByNameContainingIgnoreCase(search, pageable).map(mapper::toDTO);
        }
        return foodRepo.findAll(pageable).map(mapper::toDTO);
    }

    // --- 2. USER: Tìm kiếm (Đã sửa tên hàm gọi Repo) ---
    @Transactional(readOnly = true)
    public List<FoodDTO> searchFoods(Long categoryId, String keyword) {
        List<Food> foods;

        if (categoryId != null && keyword != null && !keyword.isEmpty()) {
            // Sửa: findByCategoryId -> findByCategory_Id
            foods = foodRepo.findByCategory_IdAndNameContainingIgnoreCase(categoryId, keyword);
        } else if (categoryId != null) {
            // Sửa: findByCategoryId -> findByCategory_Id
            foods = foodRepo.findByCategory_Id(categoryId);
        } else if (keyword != null && !keyword.isEmpty()) {
            foods = foodRepo.findByNameContainingIgnoreCase(keyword);
        } else {
            foods = foodRepo.findAll();
        }

        return foods.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FoodDTO getById(Long id) {
        Food food = foodRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy món ăn"));
        return mapper.toDTO(food);
    }

    @Transactional(readOnly = true)
    public List<FoodDTO> getByRestaurant(Long restaurantId) {
        // Gọi hàm đã sửa tên trong Repo
        return foodRepo.findByRestaurant_Id(restaurantId).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    // --- 3. CREATE ---
    @Transactional
    public FoodDTO create(FoodDTO dto) {
        Restaurant restaurant = restaurantRepo.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Nhà hàng không tồn tại"));
        
        Food food = mapper.toEntity(dto);
        food.setRestaurant(restaurant);

        // Lưu Category
        if (dto.getCategoryId() != null) {
            Category category = categoryRepo.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Danh mục không tồn tại"));
            food.setCategory(category);
        }
        
        return mapper.toDTO(foodRepo.save(food));
    }

    // --- 4. UPDATE ---
    @Transactional
    public FoodDTO update(Long id, FoodDTO dto) {
        Food food = foodRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy món ăn"));
        
        if (dto.getName() != null) food.setName(dto.getName());
        if (dto.getPrice() != null) food.setPrice(dto.getPrice());
        if (dto.getImage() != null) food.setImage(dto.getImage());
        if (dto.getDescription() != null) food.setDescription(dto.getDescription());
        
        // Cập nhật Nhà hàng
        if (dto.getRestaurantId() != null) {
            Long currentResId = (food.getRestaurant() != null) ? food.getRestaurant().getId() : null;
            if (!dto.getRestaurantId().equals(currentResId)) {
                Restaurant restaurant = restaurantRepo.findById(dto.getRestaurantId())
                        .orElseThrow(() -> new ResourceNotFoundException("Nhà hàng mới không tồn tại"));
                food.setRestaurant(restaurant);
            }
        }

        // Cập nhật Category
        if (dto.getCategoryId() != null) {
            Long currentCatId = (food.getCategory() != null) ? food.getCategory().getId() : null;
            if (!dto.getCategoryId().equals(currentCatId)) {
                Category category = categoryRepo.findById(dto.getCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("Danh mục không tồn tại"));
                food.setCategory(category);
            }
        }
        
        return mapper.toDTO(foodRepo.save(food));
    }

    @Transactional
    public void delete(Long id) {
        if (!foodRepo.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy món ăn để xóa");
        }
        foodRepo.deleteById(id);
    }
}