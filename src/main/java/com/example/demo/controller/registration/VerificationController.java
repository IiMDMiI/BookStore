package com.example.demo.controller.registration;

import com.example.demo.exception.UserPersistenceException;
import com.example.demo.exception.UserValidationException;
import com.example.demo.security.service.UserRegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/verification")
@AllArgsConstructor
public class VerificationController {
    private final UserRegistrationService registrationService;

    @GetMapping
    private String verify(@RequestParam("token") String token) {
        try {
            registrationService.enableUser(token);
        } catch (UserValidationException | UserPersistenceException e) {
            throw new RuntimeException(e);
        }
        return "login";
    }
}
