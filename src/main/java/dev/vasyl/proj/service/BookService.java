package dev.vasyl.proj.service;

import dev.vasyl.proj.dto.BookDto;
import dev.vasyl.proj.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(long id);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void deleteById(Long id);
}
