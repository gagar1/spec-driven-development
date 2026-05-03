package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the User Management API.
 * 
 * @spec: US1-T006 - Security configuration
 * 
 * This configuration sets up basic security for the application including
 * password encoding and HTTP security settings. It provides a foundation
 * for authentication and authorization.
 * 
 * Key Features:
 * - BCrypt password encoding
 * - Basic HTTP security configuration
 * - Public access to API documentation
 * - H2 console access for development
 * 
 * @author SDD Framework
 * @version 1.0
 * @since 2024-01-01
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configure password encoder using BCrypt.
     * 
     * @spec: US1-T006 - Password encoding configuration
     * 
     * BCrypt is used for secure password hashing with salt.
     * 
     * @return PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // @spec: US1-T006 - BCrypt password encoder
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure HTTP security settings.
     * 
     * @spec: US1-T006 - HTTP security configuration
     * 
     * This configuration allows public access to all endpoints for simplicity
     * in the demo. In production, proper authentication would be required.
     * 
     * @param http HttpSecurity configuration
     * @return SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // @spec: US1-T006 - Security filter chain configuration
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for API
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll() // Allow API access
                .requestMatchers("/swagger-ui/**").permitAll() // Allow Swagger UI
                .requestMatchers("/v3/api-docs/**").permitAll() // Allow OpenAPI docs
                .requestMatchers("/h2-console/**").permitAll() // Allow H2 console
                .requestMatchers("/actuator/**").permitAll() // Allow actuator endpoints
                .anyRequest().authenticated()
            )
            .headers(headers -> headers
                .frameOptions().disable() // Allow H2 console frames
            );
        
        return http.build();
    }
}