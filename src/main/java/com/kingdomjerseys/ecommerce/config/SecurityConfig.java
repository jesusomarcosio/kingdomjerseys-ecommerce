package com.kingdomjerseys.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** Panel: todo /admin/** exige rol ADMIN, excepto la pantalla de login. */
    @Bean
    @Order(1)
    public SecurityFilterChain adminChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/admin/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/login").permitAll()
                        .anyRequest().hasRole("ADMIN")
                )
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
                        .usernameParameter("correo")
                        .passwordParameter("contrasenia")
                        .defaultSuccessUrl("/admin/productos", true)
                        .failureUrl("/admin/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")              // POST (CSRF activo)
                        .logoutSuccessUrl("/admin/login?salio")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );
        // CSRF queda habilitado por defecto
        return http.build();
    }

    /** Tienda: pública. El login de clientes no usa Spring Security todavía (deuda técnica). */
    @Bean
    @Order(2)
    public SecurityFilterChain tiendaChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")) // solo consola H2
                .headers(h -> h.frameOptions(f -> f.sameOrigin()))            // consola H2 usa frames
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}