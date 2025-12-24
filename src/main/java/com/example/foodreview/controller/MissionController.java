package com.example.foodreview.controller;

import com.example.foodreview.dto.MissionDTO;
import com.example.foodreview.model.Mission; // <-- Nhớ import model Mission
import com.example.foodreview.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
@CrossOrigin
public class MissionController {

    private final MissionService missionService;

    // --- 1. API CHO USER (Xem & Nhận thưởng) ---

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MissionDTO>> getMissions(@PathVariable Long userId) {
        return ResponseEntity.ok(missionService.getMissionsForUser(userId));
    }

    @PostMapping("/{missionId}/claim")
    public ResponseEntity<String> claimReward(@PathVariable Long missionId, @RequestParam Long userId) {
        try {
            return ResponseEntity.ok(missionService.claimReward(userId, missionId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- 2. API CHO ADMIN (Thêm & Xóa nhiệm vụ) ---

    // Admin tạo nhiệm vụ mới
    @PostMapping("/create")
    public ResponseEntity<Mission> createMission(@RequestBody Mission mission) {
        return ResponseEntity.ok(missionService.createMission(mission));
    }

    // Admin xóa nhiệm vụ
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMission(@PathVariable Long id) {
        missionService.deleteMission(id);
        return ResponseEntity.ok("Đã xóa nhiệm vụ thành công");
    }
}