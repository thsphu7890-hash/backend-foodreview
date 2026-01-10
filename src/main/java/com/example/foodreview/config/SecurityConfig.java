package com.example.foodreview.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                // 1. Công khai các API đăng nhập/đăng ký
                .requestMatchers("/auth/**", "/api/auth/**", "/login", "/register").permitAll()
                
                // 2. Cho phép TRUY CẬP CÔNG KHAI (GET) để hiển thị trang chủ không bị lỗi 403
                .requestMatchers(HttpMethod.GET, 
                    "/api/foods/**", 
                    "/api/restaurants/**", 
                    "/api/categories/**",
                    "/api/reviews/**",
                    "/api/vouchers/**",          // Voucher chung
                    "/api/user-vouchers/**"      // 👈 Sửa lỗi 403 cho trang chủ gọi danh sách voucher có sẵn
                ).permitAll()

                // 3. Profile và các chức năng cá nhân BẮT BUỘC phải đăng nhập
                // (Nếu frontend gọi mà chưa có token thì frontend phải tự xử lý chuyển hướng)
                .requestMatchers("/api/users/profile").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/reviews/**").authenticated()
                .requestMatchers("/api/orders/**").authenticated()

                // 4. Các yêu cầu còn lại
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
            "http://localhost:5173", 
            "http://localhost:3000",
            "https://fontent-reviewfood.vercel.app" 
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}