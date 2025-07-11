package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.exception.BookPersistenceException;
import com.example.demo.exception.BookValidationException;
import com.example.demo.exception.UserPersistenceException;
import com.example.demo.exception.UserValidationException;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repo;
    @Override
    public Optional<User> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repo.findByEmail(email);
    }

    @Override
    @Transactional
    public User save(User user) throws UserPersistenceException, UserValidationException {
        try {
            return repo.save(user);
        } catch (DataAccessException ex) {
            throw new UserPersistenceException("Failed to save user: " + ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            throw new UserValidationException("Invalid user data: " + ex.getMessage(), ex);
        }
    }
}
