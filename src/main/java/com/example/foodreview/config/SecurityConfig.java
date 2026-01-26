package com.example.foodreview.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                // 1. API công khai
                .requestMatchers("/api/auth/**", "/error", "/uploads/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/foods/**", "/api/categories/**").permitAll()
                // Rất quan trọng: Cho phép lệnh OPTIONS (Pre-flight) của trình duyệt
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 2. API dành cho User & Admin (Chỉ cần đăng nhập)
                .requestMatchers("/api/orders/my-orders", "/api/orders/create").authenticated()
                .requestMatchers("/api/users/profile").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/orders/{id}").authenticated()

                // 3. API dành riêng cho ADMIN & DRIVER (Trang quản lý)
                .requestMatchers(HttpMethod.GET, "/api/orders").hasAnyAuthority("ADMIN", "DRIVER", "ROLE_ADMIN", "ROLE_DRIVER")
                .requestMatchers("/api/orders/**").hasAnyAuthority("ADMIN", "DRIVER", "ROLE_ADMIN", "ROLE_DRIVER")
                .requestMatchers("/api/users/**").hasAnyAuthority("ADMIN", "ROLE_ADMIN")
                
                .anyRequest().authenticated()
            )
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://127.0.0.1:5173"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}