package com.example.foodreview.repository;

import com.example.foodreview.model.User;
import org.springframework.data.jpa.repository.JpaRepository; // 1. Dùng JPA cho MySQL
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
// 2. Sửa thành JpaRepository. ID của User trong MySQL thường là Long (tự tăng)
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Tìm user bằng username
    Optional<User> findByUsername(String username);

    // Tìm bằng email
    Optional<User> findByEmail(String email);
    
    // Kiểm tra tồn tại
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Tìm danh sách theo vai trò
    List<User> findByRole(String role);
}