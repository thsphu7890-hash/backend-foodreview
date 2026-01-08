package com.example.foodreview.controller;

import com.example.foodreview.dto.FoodDTO;
import com.example.foodreview.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Cho phép React gọi API không bị chặn
public class FoodController {

    private final FoodService foodService;

    // API Lấy danh sách món ăn (Hỗ trợ phân trang & tìm kiếm)
    // React gọi: /api/foods?page=0&size=4&search=bún
    @GetMapping
    public ResponseEntity<Page<FoodDTO>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            // @PageableDefault: Nếu React không gửi page/size, mặc định lấy trang 0, 10 món, sắp xếp mới nhất
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(foodService.getAllFoods(search, categoryId, pageable));
    }

    // API Lấy danh sách món theo Nhà Hàng (Hỗ trợ phân trang)
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<Page<FoodDTO>> getByRestaurant(
            @PathVariable Long restaurantId,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(foodService.getByRestaurant(restaurantId, pageable));
    }

    // ... Các API create, update, delete, getById giữ nguyên
    @GetMapping("/{id}")
    public ResponseEntity<FoodDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(foodService.getById(id));
    }

    @PostMapping
    public ResponseEntity<FoodDTO> create(@RequestBody FoodDTO dto) {
        return ResponseEntity.ok(foodService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodDTO> update(@PathVariable Long id, @RequestBody FoodDTO dto) {
        return ResponseEntity.ok(foodService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        foodService.delete(id);
        return ResponseEntity.ok().build();
    }
}