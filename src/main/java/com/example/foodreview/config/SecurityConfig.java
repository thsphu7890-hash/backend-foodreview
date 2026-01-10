package com.example.foodreview.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // 👇 Import thêm cái này để phân quyền GET/POST
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
            .csrf(AbstractHttpConfigurer::disable) // Tắt CSRF vì dùng JWT stateless
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Kích hoạt CORS
            .authorizeHttpRequests(auth -> auth
                // 1. API Xác thực (Login/Register) -> Công khai hoàn toàn
                .requestMatchers("/auth/**", "/api/auth/**").permitAll()
                
                // 2. Các API Công khai (GET - Chỉ xem)
                .requestMatchers(HttpMethod.GET, 
                    "/api/foods/**", 
                    "/api/restaurants/**", 
                    "/api/categories/**",
                    "/api/vouchers/**" // Xem voucher
                ).permitAll()

                // 3. Review: Xem thì công khai, nhưng Viết (POST) thì phải đăng nhập
                .requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/reviews/**").authenticated()

                // 4. Các API khác (Đặt hàng, User info,...) -> Bắt buộc có Token
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
        
        // 👇 Cấu hình danh sách tên miền được phép gọi API
        configuration.setAllowedOrigins(List.of(
            "http://localhost:5173", // Frontend chạy local
            "http://localhost:3000",
            "https://fontent-reviewfood.vercel.app" // 👈 Link Vercel chính thức
        ));
        
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // Cho phép gửi cookie/credential nếu cần
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}