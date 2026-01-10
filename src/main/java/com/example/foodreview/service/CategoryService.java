package com.example.foodreview.service;

import java.util.List;
import java.util.stream.Collectors;

// ĐÚNG: Import Category từ model của bạn
import com.example.foodreview.model.Category;
import com.example.foodreview.repository.CategoryRepository;
import com.example.foodreview.dto.CategoryDTO;
import com.example.foodreview.mapper.CategoryMapper;
import com.example.foodreview.exception.ResourceNotFoundException; // Dùng exception bạn đã tạo

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor // Lombok sẽ tự tạo Constructor để inject repo và mapper
public class CategoryService {

    private final CategoryRepository repo;
    private final CategoryMapper mapper;

    public List<CategoryDTO> getAll() {
        return repo.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO create(CategoryDTO dto) {
        Category entity = mapper.toEntity(dto);
        Category savedEntity = repo.save(entity);
        return mapper.toDTO(savedEntity);
    }

    public CategoryDTO update(Long id, CategoryDTO dto) {
        // Sử dụng ResourceNotFoundException để trả về mã 404
        Category entity = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        mapper.updateEntity(entity, dto);
        return mapper.toDTO(repo.save(entity));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        repo.deleteById(id);
    }
}