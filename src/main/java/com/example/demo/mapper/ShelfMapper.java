package com.example.demo.mapper;

import com.example.demo.dto.ShelfDTO;
import com.example.demo.entity.Shelf;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShelfMapper {
    ShelfDTO toDto(Shelf shelf);
}
