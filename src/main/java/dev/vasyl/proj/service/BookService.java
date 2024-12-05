package dev.vasyl.proj.service;

import dev.vasyl.proj.dto.BookDto;
import dev.vasyl.proj.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    List<BookDto> findAll();

    BookDto findById(long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);
}
