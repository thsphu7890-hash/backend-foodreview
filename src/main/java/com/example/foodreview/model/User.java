package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User implements UserDetails { // 👈 1. THÊM implements UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;
    private String avatar;

    // --- CÁC TRƯỜNG CHO ORDER SERVICE ---
    private String fullName;
    private String phone;
    private String address;
    // ------------------------------------

    private String role; // "ROLE_USER", "ROLE_ADMIN"

    @Column(columnDefinition = "boolean default false")
    private Boolean locked;

    @Column(columnDefinition = "integer default 0")
    private int points = 0;

    @PrePersist
    public void prePersist() {
        if (this.role == null) this.role = "ROLE_USER";
        if (this.locked == null) this.locked = false;
        if (this.points < 0) this.points = 0;
    }

    // 👇 2. CÁC HÀM BẮT BUỘC CỦA SPRING SECURITY (UserDetails) 👇

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Chuyển chuỗi "ROLE_USER" thành Quyền để Spring hiểu
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Tài khoản không bao giờ hết hạn
    }

    @Override
    public boolean isAccountNonLocked() {
        // Nếu locked = true thì hàm này trả về false (đã bị khóa)
        // Nếu locked = false hoặc null thì trả về true (không bị khóa)
        return !Boolean.TRUE.equals(locked);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Mật khẩu không bao giờ hết hạn
    }

    @Override
    public boolean isEnabled() {
        return true; // Tài khoản luôn kích hoạt
    }
}