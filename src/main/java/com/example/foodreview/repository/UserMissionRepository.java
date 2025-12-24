package com.example.foodreview.repository;

import com.example.foodreview.model.UserMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMissionRepository extends JpaRepository<UserMission, Long> {
    
    /**
     * Tìm bản ghi UserMission dựa trên userId và missionId
     * Giúp kiểm tra xem user này đã tương tác/nhận quà của nhiệm vụ này chưa
     */
    Optional<UserMission> findByUserIdAndMissionId(Long userId, Long missionId);
}