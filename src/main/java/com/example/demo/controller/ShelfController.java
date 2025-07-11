package com.example.demo.controller;


import com.example.demo.dto.BookDTO;
import com.example.demo.dto.ShelfDTO;
import com.example.demo.entity.Book;
import com.example.demo.entity.Shelf;
import com.example.demo.mapper.BookMapper;
import com.example.demo.mapper.ShelfMapper;
import com.example.demo.service.ShelfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/shelves")
@AllArgsConstructor
@Tag(name = "Shelves", description = "Use to work shelves")
public class ShelfController {
    private final ShelfService shelfService;
    private final ShelfMapper shelfMapper;

    @GetMapping
    @Operation(
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully returned shelves",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(
                                                    schema = @Schema(implementation = ShelfDTO.class)
                                            )
                                    )
                            }
                    )
            }
    )
    public List<ShelfDTO> findAllShelves() {
        List<Shelf> shelves = shelfService.findAllShelves();

        return shelves.stream()
                .map(shelfMapper::toDto)
                .collect(Collectors.toList());

    }
}
