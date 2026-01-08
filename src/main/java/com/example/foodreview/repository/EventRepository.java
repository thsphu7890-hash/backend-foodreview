package com.example.foodreview.repository;

import com.example.foodreview.model.Event;
import com.example.foodreview.model.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // Tìm sự kiện theo trạng thái
    List<Event> findByStatus(EventStatus status);
    
    // Sắp xếp sự kiện mới nhất lên đầu
    List<Event> findAllByOrderByDateDesc();
}