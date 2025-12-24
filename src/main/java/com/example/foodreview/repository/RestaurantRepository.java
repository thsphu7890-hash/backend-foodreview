package com.example.foodreview.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.foodreview.model.Restaurant;



public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByCategoryId(Long categoryId);
}
