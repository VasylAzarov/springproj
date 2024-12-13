package dev.vasyl.proj.service.impl;

import dev.vasyl.proj.dto.book.BookDto;
import dev.vasyl.proj.dto.book.BookDtoWithoutCategoryIds;
import dev.vasyl.proj.dto.book.CreateBookRequestDto;
import dev.vasyl.proj.exception.EntityAlreadyExistsException;
import dev.vasyl.proj.exception.EntityNotFoundException;
import dev.vasyl.proj.mapper.BookMapper;
import dev.vasyl.proj.model.Book;
import dev.vasyl.proj.repository.BookRepository;
import dev.vasyl.proj.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto bookDto) {
        if (bookRepository.existsByIsbn(bookDto.getIsbn())) {
            throw new EntityAlreadyExistsException("book with isbn ["
                    + bookDto.getIsbn()
                    + "] already exist");
        }
        Book book = bookRepository.save(bookMapper.toModel(bookDto));
        return bookMapper.toDto(book);
    }

    @Override
    public Page<BookDtoWithoutCategoryIds> findAll(Pageable pageable) {
        return bookMapper.toBookDtoWithoutCategoryId(bookRepository.findAll(pageable));
    }

    @Override
    public BookDto findById(long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find Book entity by id: " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto requestDto) {
        Book existingBook = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find Book entity by id: " + id));
        bookMapper.updateBookFromDto(requestDto, existingBook);
        Book updatedBook = bookRepository.save(existingBook);
        return bookMapper.toDto(updatedBook);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

}
