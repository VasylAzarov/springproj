package dev.vasyl.proj.service;

import dev.vasyl.proj.dto.BookDto;
import dev.vasyl.proj.dto.CreateBookRequestDto;
import dev.vasyl.proj.mapper.BookMapper;
import dev.vasyl.proj.model.Book;
import dev.vasyl.proj.repository.BookRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto bookDto) {
        Book book = bookRepository.save(bookMapper.toModel(bookDto));
        return bookMapper.toDto(book);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can`t find Book entity bi id: " + id));
        return bookMapper.toDto(book);
    }
}
