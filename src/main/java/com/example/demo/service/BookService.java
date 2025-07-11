package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.exception.BookPersistenceException;
import com.example.demo.exception.BookValidationException;

import java.util.List;
import java.util.Optional;


public interface BookService {
    List<Book> findAllBooks();

    Book save(Book book) throws BookPersistenceException, BookValidationException;

    Optional<Book> findById(Long id);

    void deleteById(Long id);
}
