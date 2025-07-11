package com.example.demo.controller.book;

import com.example.demo.entity.Book;
import com.example.demo.entity.User;
import com.example.demo.exception.BookPersistenceException;
import com.example.demo.exception.BookValidationException;
import com.example.demo.security.user.AuthUser;
import com.example.demo.service.BookService;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/books/add")
@AllArgsConstructor
public class BookUploadController {
    private static final String BOOKS_DIRECTORY = System.getProperty("user.home") + "/books";
    private final BookService bookService;
    private final UserService userService;

    @PostMapping
    public String uploadBookForm(
            @AuthenticationPrincipal AuthUser userDetails,
            @RequestParam MultipartFile bookFile, @RequestParam String name,
            @RequestParam String author, ModelMap modelMap) throws IOException, BookPersistenceException, BookValidationException {

        Path booksPath = Paths.get(BOOKS_DIRECTORY);
        if (!Files.exists(booksPath)) {
            Files.createDirectories(booksPath);
        }

        if (!bookFile.isEmpty()) {
            String originalFilename = bookFile.getOriginalFilename();
            Path filePath = booksPath.resolve(originalFilename);
            bookFile.transferTo(filePath.toFile());


            saveBookToDB(userDetails, name, author, filePath);
        }

        modelMap.addAttribute("name", name);
        modelMap.addAttribute("author", author);
        modelMap.addAttribute("bookFile", bookFile);
        return "fileUploadView";
    }

    private void saveBookToDB(AuthUser userDetails, String name, String author,  Path filePath) throws BookPersistenceException, BookValidationException {
        User user = userService.findById(Long.valueOf(userDetails.userId())).
                orElseThrow(() -> new RuntimeException("User not found"));

        Book book = Book.builder()
                .user(user)
                .name(name)
                .author(author)
                .filePath(filePath.toString())
                .build();

        bookService.save(book);
    }

}
