package com.example.demo.exception;

public class UserValidationException extends Throwable {
    public UserValidationException(String s, IllegalArgumentException ex) {
        super(s, ex);
    }
}
