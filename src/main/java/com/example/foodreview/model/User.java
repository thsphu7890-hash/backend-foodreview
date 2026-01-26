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

    private String username; // Dùng cái này để đăng nhập
    private String password;
    private String email;
    private String fullName;
    private String role; 
    private String avatar; 
    private String phone;
    private String address;
    private int points; 
    private boolean locked; 

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String userRole = (role == null || role.isEmpty()) ? "USER" : role.toUpperCase();
        // Trả về cả 2 định dạng để dứt điểm lỗi lệch tiền tố ROLE_
        return List.of(
            new SimpleGrantedAuthority("ROLE_" + userRole),
            new SimpleGrantedAuthority(userRole)
        );
    }

    @Override
    public String getUsername() { 
        return username; // Sửa lại: Trả về field username thực tế
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return !locked; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}