package com.example.demo.service.impl;

import com.example.demo.entity.Book;
import com.example.demo.exception.BookPersistenceException;
import com.example.demo.exception.BookValidationException;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Primary
public class BookServiceImpl implements BookService {

    private final BookRepository repo;
    @Override
    public List<Book> findAllBooks() {
        return repo.findAll();
    }

    @Override
    @Transactional
    public Book save(Book book) throws BookPersistenceException, BookValidationException {
        try {
            return repo.save(book);
        } catch (DataAccessException ex) {
            throw new BookPersistenceException("Failed to save book: " + ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            throw new BookValidationException("Invalid book data: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
