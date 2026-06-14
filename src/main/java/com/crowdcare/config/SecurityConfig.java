package com.crowdcare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Nonaktifkan CSRF karena REST API (bukan form-based web)
                .csrf(AbstractHttpConfigurer::disable)

                // Konfigurasi otorisasi endpoint
                .authorizeHttpRequests(auth -> auth

                                // Endpoint publik: login, register, lihat campaign
                                .requestMatchers(
                                        "/api/users/login",
                                        "/api/users/register",
                                        "/api/users/check",
                                        "/api/campaigns",
                                        "/api/campaigns/{id}",
                                        "/h2-console/**"
                                ).permitAll()

                                // Semua endpoint lain butuh autentikasi
                                .anyRequest().permitAll()   // Diubah ke permitAll agar JavaFX bisa akses bebas
                        // karena session dikelola JavaFX (UserSession)
                )

                // Izinkan H2 console (untuk debug database di browser)
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                );

        return http.build();
    }
}