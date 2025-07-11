package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "User data")
public class UserResponse {
    @Schema(description = "Unique user identifier", example = "1")
    @NotNull
    private Long id;

    @Schema(description = "User name", example = "Pickman")
    @NotNull
    private String name;

    @Schema(description = "User's email", example = "pickman@gmail.com")
    @NotNull
    private String email;
}
