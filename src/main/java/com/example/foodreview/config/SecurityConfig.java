package com.example.foodreview.config;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**", "/uploads/**", "/error").permitAll()
                
                // Các API công khai (GET)
                .requestMatchers(HttpMethod.GET, 
                    "/api/food/**",       
                    "/api/restaurants/**", 
                    "/api/categories/**", 
                    "/api/comments/**",
                    "/api/vouchers/**",  
                    "/api/user-vouchers/**", // Đã thêm dòng này từ bước trước để sửa lỗi 403
                    "/api/reviews/**"  
                ).permitAll()
                // Voucher có thể POST công khai
                .requestMatchers(HttpMethod.POST, "/api/vouchers/**").permitAll()
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 👇 [ĐÃ SỬA ĐOẠN NÀY] Dùng setAllowedOriginPatterns thay vì setAllowedOrigins
        // Giúp chấp nhận mọi subdomain của Vercel (vd: link preview, link chính...)
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:*",              // Chấp nhận mọi port localhost (3000, 5173...)
            "https://*.vercel.app"             // Chấp nhận TẤT CẢ các link đuôi .vercel.app
        ));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}