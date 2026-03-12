package com.bsmaa.alumni_connect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for Thunder Client testing
                .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Allow everything so your manual controller works
                );
        return http.build();
    }
}
