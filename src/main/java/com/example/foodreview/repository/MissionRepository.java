package com.example.foodreview.repository;

import com.example.foodreview.model.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {
    // Hiện tại chưa cần query phức tạp, dùng các hàm có sẵn của JpaRepository là đủ
}