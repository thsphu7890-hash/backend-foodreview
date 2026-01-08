package com.example.foodreview.service;

import com.example.foodreview.model.Event;
import com.example.foodreview.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAllByOrderByDateDesc();
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    // --- MỚI: Hàm lấy sự kiện theo ID ---
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    // --- MỚI: Hàm Sửa sự kiện ---
    public Event updateEvent(Long id, Event eventDetails) {
        return eventRepository.findById(id).map(existingEvent -> {
            // Cập nhật từng trường thông tin
            existingEvent.setTitle(eventDetails.getTitle());
            existingEvent.setImage(eventDetails.getImage());
            existingEvent.setDate(eventDetails.getDate());
            existingEvent.setTime(eventDetails.getTime());
            existingEvent.setLocation(eventDetails.getLocation());
            existingEvent.setStatus(eventDetails.getStatus());
            existingEvent.setDescription(eventDetails.getDescription());
            existingEvent.setInterested(eventDetails.getInterested());
            
            return eventRepository.save(existingEvent);
        }).orElseThrow(() -> new RuntimeException("Không tìm thấy sự kiện với ID: " + id));
    }

    // --- MỚI: Hàm Xóa sự kiện ---
    public void deleteEvent(Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
        } else {
            throw new RuntimeException("Không tìm thấy sự kiện với ID: " + id);
        }
    }
}