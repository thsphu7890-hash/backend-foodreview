package com.example.foodreview.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
                // 1. Các API xác thực (Login/Register) -> Công khai
                .requestMatchers("/auth/**", "/api/auth/**", "/login", "/register", "/public/**").permitAll()
                
                // 2. === QUAN TRỌNG: Mở quyền xem dữ liệu (Sửa lỗi 403) ===
                .requestMatchers(
                    "/api/restaurants/**", 
                    "/api/foods/**", 
                    "/api/categories/**",
                    "/api/reviews/**" // Thêm cái này để xem review không cần login
                ).permitAll()

                // 3. Voucher -> Công khai
                .requestMatchers("/api/vouchers/**").permitAll() 
                
                // 4. Các API còn lại -> Bắt buộc có Token
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
        // Cho phép các domain Frontend gọi vào
        configuration.setAllowedOrigins(List.of(
            "http://localhost:5173", 
            "http://localhost:3000", 
            "https://fontent-reviewfood.vercel.app",
            "https://fontent-reviewfood-j45sejm8c-thsphus-projects.vercel.app" // Thêm domain vercel cụ thể của bạn để chắc chắn không lỗi CORS
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}