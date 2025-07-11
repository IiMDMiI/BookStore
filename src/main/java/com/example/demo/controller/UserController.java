package com.example.demo.controller;

import com.example.demo.dto.CreateUserRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.exception.UserPersistenceException;
import com.example.demo.exception.UserValidationException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
@Tag(name = "Users", description = "Use to work with users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @PostMapping
    @Operation(
            summary = "Create a new user",
            description = "Creates a new user with the provided details",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User was created successfully",
                            content = @Content(
                                    schema = @Schema(implementation = UserResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input",
                            content = @Content
                    ),
            }
    )
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest body) {

        Optional<User> user = userService.findByEmail(body.getEmail());
        if (user.isPresent()) {
            return ResponseEntity.badRequest()
                    .header("X-Error-Message", "User with this email already exists")
                    .body(null);
        }

        User newUser = User.builder()
                .name(body.getName())
                .email(body.getEmail())
                .passHash(passwordEncoder.encode(body.getPassword()))
                .build();

        try {
            User savedUser = userService.save(newUser);
            UserResponse responseBody = userMapper.toDto(savedUser);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(responseBody);
        } catch (UserValidationException ex) {
            return ResponseEntity.badRequest()
                    .header("X-Error-Message", ex.getMessage())
                    .body(null);
        } catch (UserPersistenceException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("X-Error-Message", "Failed to save book: " + ex.getMessage())
                    .body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("X-Error-Message", "Unexpected error occurred")
                    .body(null);
        }
    }
}
