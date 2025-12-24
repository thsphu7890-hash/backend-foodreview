package com.example.foodreview.service;

import com.example.foodreview.model.User;
import com.example.foodreview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 1. Lấy danh sách user (Đã có)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    // 2. Upload Avatar (Đã có - Giữ nguyên code của bạn)
    public String updateAvatar(Long userId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File trống");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User không tồn tại"));

        try {
            String fileName = "avatar_" + UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadDir = Paths.get("uploads");
            
            if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);

            Path destination = uploadDir.resolve(fileName);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            }

            String fileUrl = "/uploads/" + fileName;
            user.setAvatar(fileUrl);
            userRepository.save(user);

            return fileUrl;
        } catch (IOException e) {
            throw new RuntimeException("Lỗi upload: " + e.getMessage());
        }
    }

    // 3. THÊM MỚI: Khóa / Mở khóa tài khoản (Cho Admin Dashboard)
    public User toggleLockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User không tồn tại"));
        
        // Đảo ngược trạng thái khóa (True -> False, False -> True)
        if (user.getLocked() == null) user.setLocked(false);
        user.setLocked(!user.getLocked());
        
        return userRepository.save(user);
    }
}