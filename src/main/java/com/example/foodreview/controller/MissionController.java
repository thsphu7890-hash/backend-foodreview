package com.example.foodreview.controller;

import com.example.foodreview.model.Mission;
import com.example.foodreview.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/missions") // <--- Đây là địa chỉ React đang gọi
@CrossOrigin(origins = "*")      // Cho phép React truy cập
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @GetMapping
    public ResponseEntity<List<Mission>> getAll() {
        return ResponseEntity.ok(missionService.getAllMissions());
    }

    @PostMapping
    public ResponseEntity<Mission> create(@RequestBody Mission mission) {
        return ResponseEntity.ok(missionService.createMission(mission));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        missionService.deleteMission(id);
        return ResponseEntity.ok().build();
    }
}