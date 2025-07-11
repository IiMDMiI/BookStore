package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Shelf response data transfer object")
public class ShelfDTO {
    @Schema(description = "Unique identifier of the shelf", example = "1")
    @NotNull
    private Long id;

    @Schema(description = "Name of the shelf", example = "Cosmic Horror")
    @NotNull
    private String name;

    @ArraySchema(
            schema = @Schema(
                    description = "List of books on this shelf",
                    implementation = BookDTO.class)

    )
    private List<BookDTO> books;
}
