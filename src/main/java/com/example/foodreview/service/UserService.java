package com.example.foodreview.service;

import com.example.foodreview.model.User;
import com.example.foodreview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional; // Nhớ import cái này

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 1. Lấy tất cả user
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 2. Lấy user theo ID
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user"));
    }

    // 3. Khóa / Mở khóa tài khoản
    public User toggleLockUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        user.setLocked(!user.isLocked()); // Đảo ngược trạng thái
        
        return userRepository.save(user);
    }
    
    // 4. Cập nhật thông tin User
    public User updateUser(Long id, User req) {
        User user = getUserById(id);
        
        // Cập nhật các trường (Chỉ cập nhật nếu có dữ liệu gửi lên)
        if (req.getFullName() != null) user.setFullName(req.getFullName());
        if (req.getPhone() != null) user.setPhone(req.getPhone());
        if (req.getAddress() != null) user.setAddress(req.getAddress());
        if (req.getAvatar() != null) user.setAvatar(req.getAvatar());

        return userRepository.save(user);
    }

    // 👇 5. THÊM HÀM NÀY ĐỂ REVIEW CONTROLLER GỌI 👇
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}