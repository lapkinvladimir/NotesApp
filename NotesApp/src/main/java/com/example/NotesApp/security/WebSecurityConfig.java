package com.example.NotesApp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("123"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests((authorize) -> {authorize
//                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
//                .requestMatchers(AntPathRequestMatcher.antMatcher("/")).permitAll();
//        }).csrf(csrf -> csrf
//                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")))
//                .formLogin(withDefaults());
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/login")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/register")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/register")).permitAll() // Добавлено
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/login")).permitAll() // Добавлено
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/notes")).permitAll()
                                .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions().disable())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")))
                .formLogin().permitAll().and().logout().permitAll();

        return http.build();
    }





}

