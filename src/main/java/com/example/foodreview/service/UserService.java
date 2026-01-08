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
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService { // 👈 BƯỚC 1: XÓA 'implements UserDetailsService' ở đây đi là hết gạch đỏ

    private final UserRepository userRepository;

    // --- Giữ lại các hàm này ---
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public String updateAvatar(Long userId, MultipartFile file) {
        if (file.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File trống");
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        try {
            String fileName = "avatar_" + UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadDir = Paths.get("uploads");
            if (!Files.exists(uploadDir)) Files.createDirectories(uploadDir);
            Path destination = uploadDir.resolve(fileName);
            try (InputStream is = file.getInputStream()) { Files.copy(is, destination, StandardCopyOption.REPLACE_EXISTING); }
            String fileUrl = "/uploads/" + fileName;
            user.setAvatar(fileUrl);
            userRepository.save(user);
            return fileUrl;
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public User toggleLockUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (user.getLocked() == null) user.setLocked(false);
        user.setLocked(!user.getLocked());
        return userRepository.save(user);
    }
}