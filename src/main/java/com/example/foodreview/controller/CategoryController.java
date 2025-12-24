package com.example.foodreview.controller;

import java.util.List;


import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.foodreview.dto.CategoryDTO;
import com.example.foodreview.service.CategoryService;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    public List<CategoryDTO> getAll() {
        return service.getAll();
    }

    @PostMapping
    public CategoryDTO create(@RequestBody CategoryDTO dto) {
        return service.create(dto);
    }
}
