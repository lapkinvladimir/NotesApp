package com.example.NotesApp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/login")).permitAll() // Разрешаем доступ к /login без аутентификации
                                .anyRequest().permitAll() // Запросы к остальным URL требуют аутентификации
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/login") // Указываем URL страницы логина
                                .loginProcessingUrl("/authenticate") // URL для обработки POST-запроса логина
                                .defaultSuccessUrl("/notes") // URL для перенаправления после успешной аутентификации
                                .permitAll()
                )
                .csrf().disable()
                .headers().frameOptions().disable().and()
                .logout().disable();

        return http.build();
    }


    @Bean
    public UserDetails user() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return User.withUsername("user")
                .password(encoder.encode("password"))
                .roles("USER")
                .build();
    }
}