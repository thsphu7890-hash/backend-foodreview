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
                // 1. API Công khai
                .requestMatchers("/api/auth/**", "/error", "/uploads/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/foods/**", "/api/categories/**", "/api/restaurants/**", "/api/events/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // 2. API cho User (Sử dụng authenticated() cho các route cá nhân)
                .requestMatchers("/api/orders/my-orders", "/api/orders/create").authenticated()
                .requestMatchers("/api/users/profile").authenticated()

                // 3. API cho Admin & Driver
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
        
        // Gộp các domain Vercel bằng Wildcard (*)
        config.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:5173",
            "http://127.0.0.1:5173",
            "https://fontent-reviewfood*.vercel.app" 
        ));
        
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}