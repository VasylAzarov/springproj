package dev.vasyl.proj.service;

import dev.vasyl.proj.dto.book.BookDto;
import dev.vasyl.proj.dto.category.CategoryDto;
import dev.vasyl.proj.dto.category.CreateCategoryRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {

    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto findById(Long id);

    CategoryDto save(CreateCategoryRequestDto categoryDto);

    CategoryDto update(Long id, CreateCategoryRequestDto categoryDto);

    void deleteById(Long id);

    Page<BookDto> getBooksById(Long id, Pageable pageable);
}
