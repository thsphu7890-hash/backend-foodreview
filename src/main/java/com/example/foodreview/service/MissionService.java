package com.example.foodreview.service;

import com.example.foodreview.model.Mission;
import com.example.foodreview.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MissionService {
    private final MissionRepository missionRepo;

    public List<Mission> getAllMissions() {
        return missionRepo.findAll();
    }

    public Mission createMission(Mission mission) {
        return missionRepo.save(mission);
    }

    public void deleteMission(Long id) {
        missionRepo.deleteById(id);
    }
}