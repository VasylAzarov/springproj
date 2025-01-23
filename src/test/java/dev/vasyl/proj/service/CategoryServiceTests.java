package dev.vasyl.proj.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dev.vasyl.proj.dto.book.BookDto;
import dev.vasyl.proj.dto.category.CategoryDto;
import dev.vasyl.proj.dto.category.CreateCategoryRequestDto;
import dev.vasyl.proj.exception.EntityNotFoundException;
import dev.vasyl.proj.mapper.BookMapper;
import dev.vasyl.proj.mapper.CategoryMapper;
import dev.vasyl.proj.model.Category;
import dev.vasyl.proj.repository.BookRepository;
import dev.vasyl.proj.repository.CategoryRepository;
import dev.vasyl.proj.service.impl.CategoryServiceImpl;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Verify that page of categories returned successfully")
    void findAll_shouldReturnPageOfCategories_whenPageableProvided() {
        Pageable pageable = mock(Pageable.class);
        Page<Category> categories = new PageImpl<>(Collections.emptyList());
        when(categoryRepository.findAll(pageable)).thenReturn(categories);
        when(categoryMapper.toCategoryDtoPage(categories)).thenReturn(Page.empty());

        Page<CategoryDto> result = categoryService.findAll(pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toCategoryDtoPage(categories);
    }

    @Test
    @DisplayName("Verify that category is returned successfully when exist")
    void findById_shouldReturnCategoryDto_whenIdExists() {
        Long id = 1L;
        Category category = new Category();
        CategoryDto categoryDto = new CategoryDto();
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.findById(id);

        assertNotNull(result);
        verify(categoryRepository).findById(id);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Verify that exception is thrown when category doesn't exist")
    void findById_shouldThrowException_whenIdDoesNotExist() {
        Long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.findById(id));

        assertEquals("Can`t find Category entity by id: " + id, exception.getMessage());
        verify(categoryRepository).findById(id);
    }

    @Test
    @DisplayName("Verify that category is saved successfully when requestDto valid")
    void save_shouldReturnSavedCategoryDto_whenRequestDtoValid() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        Category category = new Category();
        CategoryDto categoryDto = new CategoryDto();
        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.save(requestDto);

        assertNotNull(result);
        verify(categoryMapper).toModel(requestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("Verify that page of books returned successfully when category id exist")
    void getBooksById_shouldReturnPageOfBooks_whenIdExists() {
        Long id = 1L;
        Pageable pageable = mock(Pageable.class);
        Page<BookDto> books = new PageImpl<>(Collections.emptyList());
        when(bookRepository.findByCategoriesId(id, pageable))
                .thenReturn(new PageImpl<>(Collections.emptyList()));
        when(bookMapper.toBookDtoPage(any(Page.class))).thenReturn(books);

        Page<BookDto> result = categoryService.getBooksById(id, pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookRepository).findByCategoriesId(id, pageable);
        verify(bookMapper).toBookDtoPage(any(Page.class));
    }
}
