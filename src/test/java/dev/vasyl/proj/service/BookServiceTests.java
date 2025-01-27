package dev.vasyl.proj.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.vasyl.proj.dto.book.BookDto;
import dev.vasyl.proj.dto.book.BookDtoWithoutCategoryIds;
import dev.vasyl.proj.dto.book.CreateBookRequestDto;
import dev.vasyl.proj.exception.EntityAlreadyExistsException;
import dev.vasyl.proj.exception.EntityNotFoundException;
import dev.vasyl.proj.mapper.BookMapper;
import dev.vasyl.proj.model.Book;
import dev.vasyl.proj.repository.BookRepository;
import dev.vasyl.proj.service.impl.BookServiceImpl;
import java.util.Collections;
import java.util.Optional;
import dev.vasyl.proj.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {
    private final TestUtil testUtil = new TestUtil();

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Verify that correct book is saved successfully")
    void save_shouldReturnBook_whenBookSavedSuccessfully() {
        when(bookRepository.existsByIsbn(anyString())).thenReturn(false);
        when(bookMapper.toModel(any(CreateBookRequestDto.class))).thenReturn(testUtil.getOneBook());
        when(bookRepository.save(any(Book.class))).thenReturn(testUtil.getOneBook());
        when(bookMapper.toDto(any(Book.class))).thenReturn(testUtil.getOneBookDto());

        BookDto result = bookService.save(testUtil.getOneCreateBookRequestDto());

        assertNotNull(result);
        assertEquals(testUtil.getOneBookDto().getId(), result.getId());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(bookRepository, times(1)).existsByIsbn(anyString());
        verify(bookMapper, times(1)).toModel(any(CreateBookRequestDto.class));
        verify(bookMapper, times(1)).toDto(any(Book.class));
    }

    @Test
    @DisplayName("Verify that exception is thrown when saving an existing book")
    void save_shouldThrowException_whenBookAlreadyExists() {
        when(bookRepository.existsByIsbn(anyString())).thenReturn(true);

        EntityAlreadyExistsException exception = assertThrows(
                EntityAlreadyExistsException.class,
                () -> bookService.save(testUtil.getOneCreateBookRequestDto())
        );

        assertEquals("book with isbn [" + testUtil.getOneCreateBookRequestDto().getIsbn()
                + "] already exist", exception.getMessage());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Verify that list of books returned successfully")
    void findAll_shouldReturnListOfBooks_whenBooksExist() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(Collections.singletonList(testUtil.getOneBook()));
        Page<BookDtoWithoutCategoryIds> bookDtoPage =
                new PageImpl<>(Collections.singletonList(new BookDtoWithoutCategoryIds()));

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toBookDtoWithoutCategoryId(bookPage)).thenReturn(bookDtoPage);

        Page<BookDtoWithoutCategoryIds> result = bookService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(1)).toBookDtoWithoutCategoryId(bookPage);
    }

    @Test
    @DisplayName("Verify that empty list of books returned successfully")
    void findAll_shouldReturnEmptyList_whenNoBooksExist() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> emptyPage = new PageImpl<>(Collections.emptyList());

        when(bookRepository.findAll(pageable)).thenReturn(emptyPage);
        when(bookMapper.toBookDtoWithoutCategoryId(emptyPage)).thenReturn(new PageImpl<>(Collections.emptyList()));

        Page<BookDtoWithoutCategoryIds> result = bookService.findAll(pageable);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        verify(bookRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Verify that book is returned successfully when exist")
    void findById_shouldReturnBook_whenBookExists() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(testUtil.getOneBook()));
        when(bookMapper.toDto(any(Book.class))).thenReturn(testUtil.getOneBookDto());

        BookDto result = bookService.findById(1L);

        assertNotNull(result);
        assertEquals(testUtil.getOneBookDto().getId(), result.getId());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Verify that exception is thrown when book doesn't exist")
    void findById_shouldThrowException_whenBookDoesNotExist() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(1L)
        );

        assertEquals("Can`t find Book entity by id: " + 1L, exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
    }
}
