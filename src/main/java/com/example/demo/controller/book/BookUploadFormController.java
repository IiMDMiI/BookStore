package com.example.demo.controller.book;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books/form")
@AllArgsConstructor
public class BookUploadFormController {
    @GetMapping
    public String showUploadBookForm(Model model) {
        return "addBookView";
    }
}
