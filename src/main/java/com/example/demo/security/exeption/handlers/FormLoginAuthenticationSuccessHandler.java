package com.example.demo.security.exeption.handlers;

import com.example.demo.security.provider.JwtTokenProvider;
import com.example.demo.security.user.AuthUser;
import com.example.demo.security.user.AuthUserDetails;
import com.example.demo.security.user.Role;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class FormLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtTokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();

        String token = tokenProvider.createJwtToken(
                        new AuthUser(Long.toString(userDetails.getId()),
                                userDetails.getAuthorities()
                                        .stream().map(n-> Role.valueOf(n.toString()))
                                        .collect(Collectors.toList()))
        );

        ResponseCookie cookie = ResponseCookie.from("JWT", token)
                .httpOnly(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(7 * 24 * 60 * 60)  // 1 week expiration
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.sendRedirect("jwtAuth");
    }
}