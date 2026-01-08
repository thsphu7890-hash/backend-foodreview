package com.example.foodreview.repository;

import com.example.foodreview.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Tìm user bằng username
    Optional<User> findByUsername(String username);

    // 👇 QUAN TRỌNG: Phải thêm dòng này thì AuthService mới gọi được .findByEmail()
    Optional<User> findByEmail(String email);
    
    // Kiểm tra tồn tại
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Tìm danh sách theo vai trò
    List<User> findByRole(String role);
}