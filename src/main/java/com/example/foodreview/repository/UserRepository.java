package com.example.foodreview.repository;

import com.example.foodreview.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // 👇 THÊM DÒNG NÀY: Để tìm tất cả user là "ROLE_DRIVER"
    List<User> findByRole(String role);
}