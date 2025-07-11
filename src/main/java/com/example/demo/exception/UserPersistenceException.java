package com.example.demo.exception;

import org.springframework.dao.DataAccessException;

public class UserPersistenceException extends Throwable {
    public UserPersistenceException(String s, DataAccessException ex) {
        super(s, ex);
    }
}
