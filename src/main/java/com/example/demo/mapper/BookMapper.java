package com.example.demo.mapper;

import com.example.demo.dto.BookDTO;
import com.example.demo.entity.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    BookDTO toDto(Book book);
}