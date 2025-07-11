package com.example.demo.exception;

public class BookValidationException extends Throwable {
    public BookValidationException(String s, IllegalArgumentException ex) {
        super(s, ex);
    }
}
