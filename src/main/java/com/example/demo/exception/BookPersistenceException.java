package com.example.demo.exception;

import org.springframework.dao.DataAccessException;

public class BookPersistenceException extends Throwable {
    public BookPersistenceException(String s, DataAccessException ex) {
        super(s, ex);
    }
}
