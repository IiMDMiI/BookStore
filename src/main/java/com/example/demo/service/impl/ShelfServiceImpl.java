package com.example.demo.service.impl;

import com.example.demo.entity.Shelf;
import com.example.demo.repository.ShelfRepository;
import com.example.demo.service.ShelfService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Primary
public class ShelfServiceImpl implements ShelfService {
    private final ShelfRepository repo;
    @Override
    public List<Shelf> findAllShelves() {
        return repo.findAll();
    }
}
