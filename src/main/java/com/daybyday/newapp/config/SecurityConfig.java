package com.daybyday.newapp.config;

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
            .csrf().disable()  // Désactiver CSRF pour simplifier l'API
            .authorizeRequests()
                .antMatchers("/**").permitAll()  // Autoriser l'accès à toutes les URLs sans authentification Spring Security
            .and()
            .formLogin().disable()  // Désactiver le formulaire de connexion par défaut de Spring Security
            .httpBasic().disable();  // Désactiver l'authentification HTTP Basic
        
        return http.build();
    }
}
