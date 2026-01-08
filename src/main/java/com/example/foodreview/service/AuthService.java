package com.example.foodreview.service;

import com.example.foodreview.dto.LoginRequest;
import com.example.foodreview.dto.RegisterRequest;
import com.example.foodreview.dto.UserDTO;
import com.example.foodreview.model.User;
import com.example.foodreview.repository.UserRepository;
import com.example.foodreview.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public UserDTO register(RegisterRequest request) {
        log.info("Đang xử lý đăng ký cho user: {}", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên đăng nhập đã tồn tại!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email này đã được sử dụng!");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }

        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    // --- SỬA LOGIC LOGIN TẠI ĐÂY ---
    public UserDTO login(LoginRequest request) {
        log.info("Đang xử lý đăng nhập cho username: {}", request.getUsername());

        try {
            // 1. Xác thực bằng Username + Password
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai tên đăng nhập hoặc mật khẩu");
        }

        // 2. Tìm User theo Username (Thay vì Email)
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng"));

        // 3. Tạo Token
        String token = jwtService.generateToken(user);
        UserDTO userDTO = userMapper.toDTO(user);
        userDTO.setToken(token);

        return userDTO;
    }
}