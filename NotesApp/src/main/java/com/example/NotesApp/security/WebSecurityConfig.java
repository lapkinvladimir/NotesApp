package com.example.NotesApp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password("1234")
                .roles("ADMIN")
                .build();

        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("1234")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
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
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/")).permitAll() // Требуем аутентификацию для главной страницы по HTTP GET
                                .requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/login")).permitAll()
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll() // Разрешаем доступ к H2 Console без аутентификации
                                .anyRequest().permitAll() // Разрешаем доступ ко всем остальным страницам без аутентификации
                )
                .headers(headers -> headers.frameOptions().disable())
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))) // Отключаем CSRF для H2 Console
                .formLogin().loginPage("/login").permitAll().and().logout().permitAll(); // Включаем форму логина по умолчанию

        return http.build();
    }


}

