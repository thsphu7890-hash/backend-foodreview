package com.example.foodreview.service;

import com.example.foodreview.dto.LoginRequest;
import com.example.foodreview.dto.RegisterRequest;
import com.example.foodreview.dto.UserDTO;
import com.example.foodreview.model.User;
import com.example.foodreview.repository.UserRepository;
import com.example.foodreview.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // Đã xóa hằng số DEFAULT_ROLE vì Entity tự lo rồi

    /**
     * ĐĂNG KÝ TÀI KHOẢN MỚI
     */
    @Transactional
    public UserDTO register(RegisterRequest request) {
        // 1. Kiểm tra username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên đăng nhập đã tồn tại!");
        }

        // 2. Map dữ liệu từ Request sang Entity
        User user = userMapper.toEntity(request);
        
        // 3. Mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        // --- ĐÃ XÓA code setRole và setLocked thủ công ---
        // Hibernate sẽ tự động gọi @PrePersist trong Entity User để set:
        // role = "ROLE_USER" và locked = false

        // 4. Lưu và trả về
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    /**
     * ĐĂNG NHẬP
     */
    @Transactional(readOnly = true)
    public UserDTO login(LoginRequest request) {
        // 1. Tìm user
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tài khoản hoặc mật khẩu không chính xác!"));

        // 2. Kiểm tra trạng thái khóa (Null-safe)
        if (Boolean.TRUE.equals(user.getLocked())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tài khoản của bạn đã bị khóa!");
        }

        // 3. Kiểm tra mật khẩu
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Tài khoản hoặc mật khẩu không chính xác!");
        }

        // 4. Trả về kết quả
        return userMapper.toDTO(user);
    }
}