package com.example.foodreview.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String fullName;
    private String role;
    private String avatar; 
    private String phone;
    private String address;

    // ✅ 1. Điểm tích lũy (Mặc định là 0)
    private int points; 

    // ✅ 2. Trạng thái khóa (Dùng boolean nguyên thủy để xóa cảnh báo vàng)
    // Mặc định boolean trong Java là false -> Không bị khóa
    private boolean locked; 

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + (role == null ? "USER" : role)));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    // ✅ 3. Logic khóa tài khoản chuẩn
    // Nếu locked = true -> !locked = false -> Tài khoản bị khóa
    @Override
    public boolean isAccountNonLocked() { 
        return !locked; 
    }

    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}