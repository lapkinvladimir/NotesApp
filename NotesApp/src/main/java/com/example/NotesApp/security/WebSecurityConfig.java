package com.example.NotesApp.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class WebSecurityConfig{



    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("ADMIN")
                .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("user2")
                .password("1234")
                .roles("USER")
                .build();


        return new InMemoryUserDetailsManager(user, admin);

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                )
                .headers(headers -> headers.frameOptions().disable())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")));
        return http.build();
    }




}
