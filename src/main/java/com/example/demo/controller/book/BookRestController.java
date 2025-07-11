package com.example.demo.controller.book;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.CreateBookRequest;
import com.example.demo.dto.SchemesExamples;
import com.example.demo.entity.Book;
import com.example.demo.entity.User;
import com.example.demo.exception.BookPersistenceException;
import com.example.demo.exception.BookValidationException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.security.user.AuthUser;
import com.example.demo.service.BookService;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/books")
@AllArgsConstructor
@Tag(name = "Books", description = "Use to work with books")
public class BookRestController {

    private final BookService bookService;
    private final BookMapper bookMapper;
    private final UserService userService;

    @GetMapping
    @Operation(
            summary = "Get all books",
            description = "Returns a list of all books",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully returned books",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            examples = {
                                                    @ExampleObject(
                                                            name = "Example books",
                                                            value = SchemesExamples.BOOKS_ARRAY_EXAMPLE
                                                    )
                                            },
                                            array = @ArraySchema(schema = @Schema(implementation = BookDTO.class))
                                    )
                            }
                    )
            }
    )
    public List<BookDTO> findAllBooks() {
        List<Book> books = bookService.findAllBooks();
        return books.stream()
                .map(bookMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody CreateBookRequest createBookRequest,
                                              @AuthenticationPrincipal AuthUser userDetails) {

        User user = userService.findById(Long.valueOf(userDetails.userId())).
                orElseThrow(() -> new RuntimeException("User not found"));

        Book book = Book.builder().
                user(user).
                name(createBookRequest.getName()).
                author(createBookRequest.getAuthor()).
                description(createBookRequest.getDescription()).
                build();

        try {
            Book savedBook = bookService.save(book);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(bookMapper.toDto(savedBook));
        } catch (BookValidationException ex) {
            return ResponseEntity.badRequest()
                    .header("X-Error-Message", ex.getMessage())
                    .body(null);
        } catch (BookPersistenceException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("X-Error-Message", "Failed to save book: " + ex.getMessage())
                    .body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("X-Error-Message", "Unexpected error occurred")
                    .body(null);
        }
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a book",
            description = "Updates an existing book by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book successfully updated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BookDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid book data"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "User not authorized to update this book"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Book not found"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"
                    )
            }
    )
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id,
                                              @RequestBody CreateBookRequest updateBookRequest,
                                              @AuthenticationPrincipal AuthUser userDetails) {
        try {
            Book existingBook = bookService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Book not found"));

            // Check if user is authorized to update this book
            if (!existingBook.getUser().getId().equals(Long.valueOf(userDetails.userId()))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .header("X-Error-Message", "User not authorized to update this book")
                        .body(null);
            }

            if (updateBookRequest.getName() != null) {
                existingBook.setName(updateBookRequest.getName());
            }
            if (updateBookRequest.getAuthor() != null) {
                existingBook.setAuthor(updateBookRequest.getAuthor());
            }
            if (updateBookRequest.getDescription() != null) {
                existingBook.setDescription(updateBookRequest.getDescription());
            }

            Book updatedBook = bookService.save(existingBook);
            return ResponseEntity.ok(bookMapper.toDto(updatedBook));

        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("X-Error-Message", ex.getMessage())
                    .body(null);
        } catch (BookValidationException ex) {
            return ResponseEntity.badRequest()
                    .header("X-Error-Message", ex.getMessage())
                    .body(null);
        } catch (BookPersistenceException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("X-Error-Message", "Failed to update book: " + ex.getMessage())
                    .body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("X-Error-Message", "Unexpected error occurred")
                    .body(null);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a book",
            description = "Deletes a book by ID",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Book successfully deleted"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "User not authorized to delete this book"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Book not found"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"
                    )
            }
    )
    public ResponseEntity<Void> deleteBook(@PathVariable Long id,
                                           @AuthenticationPrincipal AuthUser userDetails) {
        try {
            Book book = bookService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Book not found"));

            if (!book.getUser().getId().equals(Long.valueOf(userDetails.userId()))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .header("X-Error-Message", "User not authorized to delete this book")
                        .body(null);
            }

            bookService.deleteById(id);
            return ResponseEntity.noContent().build();

        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header("X-Error-Message", ex.getMessage())
                    .body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("X-Error-Message", "Unexpected error occurred")
                    .body(null);
        }
    }
}
