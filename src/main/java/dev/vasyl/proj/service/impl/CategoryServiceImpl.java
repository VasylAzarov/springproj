package dev.vasyl.proj.service.impl;

import dev.vasyl.proj.dto.book.BookDto;
import dev.vasyl.proj.dto.category.CategoryDto;
import dev.vasyl.proj.dto.category.CreateCategoryRequestDto;
import dev.vasyl.proj.exception.EntityNotFoundException;
import dev.vasyl.proj.mapper.BookMapper;
import dev.vasyl.proj.mapper.CategoryMapper;
import dev.vasyl.proj.model.Category;
import dev.vasyl.proj.repository.BookRepository;
import dev.vasyl.proj.repository.CategoryRepository;
import dev.vasyl.proj.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryMapper.toCategoryDtoPage(categoryRepository.findAll(pageable));
    }

    @Override
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find Category entity by id: " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto categoryDto) {
        Category category = categoryRepository.save(categoryMapper.toModel(categoryDto));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryRequestDto requestDto) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find Category entity by id: " + id));
        categoryMapper.updateCategoryFromDto(requestDto, existingCategory);
        categoryRepository.save(existingCategory);
        return categoryMapper.toDto(existingCategory);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Page<BookDto> getBooksById(Long id, Pageable pageable) {
        return bookMapper.toBookDtoPage(bookRepository.findByCategoriesId(id,pageable));
    }
}
