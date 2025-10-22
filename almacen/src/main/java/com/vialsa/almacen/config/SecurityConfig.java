package com.vialsa.almacen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/productos").authenticated()   // Protege la página de productos
                        .anyRequest().permitAll()                        // El resto libre
                )
                .formLogin(form -> form
                        .loginPage("/login")                             // Página de login
                        .defaultSuccessUrl("/productos", true)           // Redirige si login correcto
                        .permitAll()
                )
                .logout(logout -> logout.permitAll());

        return http.build();
    }
}
