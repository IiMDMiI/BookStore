package com.example.demo.controller.registration;

import com.example.demo.exception.UserAlreadyExistException;
import com.example.demo.exception.UserPersistenceException;
import com.example.demo.exception.UserValidationException;
import com.example.demo.security.service.UserRegistrationService;
import com.example.demo.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
@AllArgsConstructor
public class RegistrationController {

    private final EmailService emailService;
    private final UserRegistrationService registrationService;

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "registrationView";
    }

    @PostMapping
    public String processRegistration(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "registrationView";
        }
        try {
            String token = registrationService.register(name, email, password);
            sendConfirmationEmail(email, token);
        } catch (UserAlreadyExistException | UserValidationException | UserPersistenceException ex) {
            model.addAttribute("error", ex.getMessage());
            return "registrationView";
        }
        return "redirect:/login";
    }
    private void sendConfirmationEmail(String toEmail, String token) {
        emailService.sendSimpleEmail(toEmail, "account activation", "http://localhost:8080/verification?token=" + token);
    }
}