package com.example.demo.service;

import com.example.demo.entity.Book;
import com.example.demo.entity.User;
import com.example.demo.exception.UserPersistenceException;
import com.example.demo.exception.UserValidationException;


import java.util.Optional;

public interface UserService {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    User save(User user) throws UserPersistenceException, UserValidationException;
}
