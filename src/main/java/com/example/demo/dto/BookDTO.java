package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Book response data transfer object")
public class BookDTO {

    @Schema(description = "Unique identifier of the book", example = "1")
    @NotNull
    private Long id;

    @Schema(description = "Name of the book", example = "Pickman's Model")
    @NotNull
    private String name;

    @Schema(description = "Author of the book", example = "H.F. Lovecraft")
    @NotNull
    private String author;

    @Schema(
            description = "Description of the book",
            example = "Pickman's Model is a short story by H.P. Lovecraft " +
                    "about a Bostonian painter named Richard Upton Pickman " +
                    "who creates extraordinarily realistic and horrifying images."
    )
    @NotNull
    private String description;
}