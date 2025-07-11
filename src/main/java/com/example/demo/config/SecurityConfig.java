package com.example.demo.config;

import com.example.demo.security.exeption.handlers.ApplicationAuthenticationEntryPoint;
import com.example.demo.security.exeption.handlers.FormLoginAuthenticationSuccessHandler;
import com.example.demo.security.filter.JwtTokenFilter;
import com.example.demo.security.service.AuthUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity
@Slf4j

public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;
    private final ApplicationAuthenticationEntryPoint authenticationEntryPoint;
    private final AuthUserDetailsService userDetailsService;
    private final FormLoginAuthenticationSuccessHandler formJwtSuccessHandler;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.
                authorizeHttpRequests(
                        authorizeHttp -> {
                            authorizeHttp.requestMatchers("/api/v1/users").permitAll();
                            authorizeHttp.requestMatchers("/jwtAuth").permitAll();
                            authorizeHttp.requestMatchers("/books/form").permitAll();
                            authorizeHttp.requestMatchers("/verification").permitAll();
                            authorizeHttp.requestMatchers("/register/**").permitAll();
                            authorizeHttp.anyRequest().authenticated();
                            //authorizeHttp.anyRequest().permitAll();
                        }
                ).addFilterBefore(jwtTokenFilter, AuthorizationFilter.class)
                .formLogin(form -> form.successHandler(formJwtSuccessHandler))
                .exceptionHandling(customizer -> customizer
                        .defaultAuthenticationEntryPointFor(
                                authenticationEntryPoint,
                                request -> request.getRequestURI().startsWith("/api/")
                        )
                )
                .userDetailsService(userDetailsService)
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}