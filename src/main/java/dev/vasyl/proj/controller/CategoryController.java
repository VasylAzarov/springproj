package dev.vasyl.proj.controller;

import dev.vasyl.proj.dto.book.BookDto;
import dev.vasyl.proj.dto.category.CategoryDto;
import dev.vasyl.proj.dto.category.CreateCategoryRequestDto;
import dev.vasyl.proj.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
@Tag(name = "Category manager",
        description = "API for managing categories")
public class CategoryController {
    private final CategoryService categoryService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get Category page",
            description = "Get Category page by page params or/and sort params."
                    + " Available for User and Admin roles")
    public Page<CategoryDto> getAll(@ParameterObject
                                @PageableDefault(size = 20, sort = "name",
                                        direction = Sort.Direction.ASC)
                                        Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get category by id",
            description = "Get category by id. Available for User and Admin roles")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Create a new category",
            description = "Create a new category. Available for Admin role")
    public CategoryDto createCategory(@RequestBody @Valid CreateCategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Update category data",
            description = "Update category data. Available for Admin role")
    public CategoryDto updateCategoryById(@PathVariable Long id,
                                  @RequestBody @Valid CreateCategoryRequestDto requestDto) {
        return categoryService.update(id, requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete category by id",
            description = "Delete category by id. Available for Admin role")
    public void deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/books")
    @Operation(summary = "Get all books by category id",
            description = "Get all books by category id. Available for User and Admin roles")
    public Page<BookDto> getBooksByCategoryId(@PathVariable Long id,
                                              @ParameterObject
                                              @PageableDefault(size = 20, sort = "title",
                                                      direction = Sort.Direction.ASC)
                                                      Pageable pageable) {
        return categoryService.getBooksById(id, pageable);
    }
}
