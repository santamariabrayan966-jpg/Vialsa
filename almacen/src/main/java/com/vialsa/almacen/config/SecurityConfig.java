package com.vialsa.almacen.config;

 codex/update-application.properties-for-mysql-config-3cp5m0

codex/update-application.properties-for-mysql-config-uekked
>>>>>>> main
import com.vialsa.almacen.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
 codex/update-application.properties-for-mysql-config-3cp5m0
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 main
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
>>>>>>> main
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

codex/update-application.properties-for-mysql-config-3cp5m0
import java.util.HashMap;
import java.util.Map;


 main
@Configuration
@EnableWebSecurity
public class SecurityConfig {

 codex/update-application.properties-for-mysql-config-3cp5m0
    private final UsuarioService usuarioService;

    public SecurityConfig(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

    @Bean
 codex/update-application.properties-for-mysql-config-uekked
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   DaoAuthenticationProvider authenticationProvider) throws Exception {
        http
                .authenticationProvider(authenticationProvider)

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
 main
main
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/login").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
 codex/update-application.properties-for-mysql-config-3cp5m0
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error")

 codex/update-application.properties-for-mysql-config-uekked
                        .failureUrl("/login?error")

 main
                        .defaultSuccessUrl("/", true)
 main
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
codex/update-application.properties-for-mysql-config-3cp5m0
                );

        http.authenticationProvider(authenticationProvider());

                )
                .httpBasic(Customizer.withDefaults());
 main

        return http.build();
    }

    @Bean
codex/update-application.properties-for-mysql-config-3cp5m0
    public PasswordEncoder passwordEncoder() {
        String encodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(encodingId, new BCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());

        DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder(encodingId, encoders);
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(NoOpPasswordEncoder.getInstance());
        return delegatingPasswordEncoder;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usuarioService);
        provider.setPasswordEncoder(passwordEncoder());
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }
}

    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
 codex/update-application.properties-for-mysql-config-uekked

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UsuarioService usuarioService,
                                                                PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(usuarioService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

 main
}

>>>>>>> main
