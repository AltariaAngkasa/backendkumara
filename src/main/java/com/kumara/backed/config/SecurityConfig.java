package com.kumara.backed.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()     // Login & Register BOLEH
                .requestMatchers("/api/customers/**").permitAll() // <-- TAMBAHKAN INI (Customer API BOLEH)
                .requestMatchers("/api/transactions/**").permitAll()
                .requestMatchers("/api/academy/**").permitAll()
                .requestMatchers("/api/links/**").permitAll()
                .requestMatchers("/api/products/**").permitAll()
                .requestMatchers("/api/users/**").permitAll()
                .requestMatchers("/photos/**").permitAll() // <-- TAMBAHKAN INI (Foto Profil BOLEH)
                .requestMatchers("/api/coupons/**").permitAll()
                .requestMatchers("/api/payments/**").permitAll()
                .anyRequest().authenticated()
            );

        return http.build();
    }
}