package com.kingdomjerseys.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // TEMPORAL: se activa en la Fase 2
                .headers(h -> h.frameOptions(f -> f.sameOrigin())) // consola H2 usa frames
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll() // TEMPORAL
                        .requestMatchers("/", "/index", "/index.html").permitAll()
                        .requestMatchers("/novedades", "/novedades.html").permitAll()
                        .requestMatchers("/iniciar-sesion", "/iniciar-sesion.html", "/registro", "/registro.html").permitAll()
                        .requestMatchers("/crear-cuenta", "/autenticacion", "/cerrar-sesion").permitAll()
                        .requestMatchers("/error", "/error.html").permitAll()
                        .requestMatchers("/static/**", "/css/**", "/js/**", "/imgs/**", "/images/**").permitAll()
                        .requestMatchers("/producto", "/producto/**", "/carrito", "/carrito/**",
                                "/datos", "/envio", "/pago", "/pago-confirmado").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

}
