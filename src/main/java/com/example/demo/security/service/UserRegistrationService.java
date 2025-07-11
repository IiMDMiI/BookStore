package com.example.demo.security.service;

import com.example.demo.entity.User;
import com.example.demo.entity.VerificationToken;
import com.example.demo.exception.UserAlreadyExistException;
import com.example.demo.exception.UserPersistenceException;
import com.example.demo.exception.UserValidationException;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerificationTokenRepository;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserRegistrationService {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final VerificationTokenRepository tokenRepository;

    public String register(String name, String email, String password) throws UserAlreadyExistException, UserValidationException, UserPersistenceException {
        String passHash = passwordEncoder.encode(password);
        Optional<User> user = userService.findByEmail(email);
        if (user.isPresent()) {
            throw new UserAlreadyExistException("User with this email already exist");
        }

        String token = UUID.randomUUID().toString();

        User newUser = User.builder().name(name)
                .email(email)
                .passHash(passHash)
                .build();

        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(newUser)
                .build();

        newUser.setVerificationToken(verificationToken);
        userService.save(newUser);
        return token;
    }

    public void enableUser(String token) throws UserValidationException, UserPersistenceException {
        Optional<VerificationToken> verificationToken = tokenRepository.findByToken(token);
        if(verificationToken.isEmpty())
            return;
        if(verificationToken.get().isExpired())
            return; //TODO: update and resend verification token

        User user = verificationToken.get().getUser();
        user.setEnabled(true);
        userService.save(user);
    }
}
