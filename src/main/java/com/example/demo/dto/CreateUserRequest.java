package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "User create request object")
public class CreateUserRequest {
    @Schema(description = "User name", example = "Pickman")
    @NotNull
    private String name;

    @Schema(description = "User's email", example = "pickman@gmail.com")
    @NotNull
    private String email;

    @Schema(
            description = "User's password",
            example = "Qaw3sdf2eq1"
    )
    @NotNull
    private String password;
}
