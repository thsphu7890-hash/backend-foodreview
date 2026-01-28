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

    @Column(nullable = false, unique = true) // Username không được null và phải duy nhất
    private String username;
    
    private String password;
    private String email;
    private String fullName;
    private String role; 
    private String avatar; 
    private String phone;
    private String address;
    
    // --- SỬA Ở ĐÂY: Dùng Integer thay vì int để tránh lỗi Null ---
    @Builder.Default
    private Integer points = 0; 
    
    // --- SỬA Ở ĐÂY: Dùng Boolean thay vì boolean ---
    @Builder.Default
    private Boolean locked = false; 

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Xử lý role rỗng an toàn
        String userRole = (role == null || role.trim().isEmpty()) ? "USER" : role.toUpperCase();
        
        // Nếu role trong DB đã có chữ ROLE_ thì không cộng thêm nữa (Tránh ROLE_ROLE_USER)
        if (userRole.startsWith("ROLE_")) {
             userRole = userRole.substring(5);
        }

        return List.of(
            new SimpleGrantedAuthority("ROLE_" + userRole),
            new SimpleGrantedAuthority(userRole)
        );
    }

    @Override
    public String getUsername() { 
        return username; 
    }

    // Các hàm dưới dùng Boolean.TRUE để tránh null
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { 
        // Nếu locked là null thì coi như chưa lock (false)
        return !Boolean.TRUE.equals(locked); 
    }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}