package com.example.demo.security.user;
import java.util.List;

public record AuthUser(String userId, List<Role> roles) {}