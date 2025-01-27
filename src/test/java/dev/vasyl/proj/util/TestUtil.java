package dev.vasyl.proj.util;

import dev.vasyl.proj.dto.book.BookDto;
import dev.vasyl.proj.dto.book.BookDtoWithoutCategoryIds;
import dev.vasyl.proj.dto.book.CreateBookRequestDto;
import dev.vasyl.proj.dto.category.CategoryDto;
import dev.vasyl.proj.dto.category.CreateCategoryRequestDto;
import dev.vasyl.proj.model.Book;
import dev.vasyl.proj.model.Category;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class TestUtil {

    public static List<Book> getListOfBooks() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Category 1");
        category.setDescription("Category 1");

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("123-12-1234-123-1");
        book1.setPrice(new BigDecimal("11.0"));
        book1.setDescription("Description 1");
        book1.setCoverImage("images.com/image1.jpg");
        book1.setCategories(Set.of(category));

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setIsbn("123-12-1234-123-2");
        book2.setPrice(new BigDecimal("22.0"));
        book2.setDescription("Description 2");
        book2.setCoverImage("images.com/image2.jpg");
        book2.setCategories(Set.of(category));

        Book book3 = new Book();
        book3.setId(3L);
        book3.setTitle("Book 3");
        book3.setAuthor("Author 3");
        book3.setIsbn("123-12-1234-123-3");
        book3.setPrice(new BigDecimal("33.0"));
        book3.setDescription("Description 3");
        book3.setCoverImage("images.com/image3.jpg");
        book3.setCategories(Set.of(category));

        List<Book> expected = new ArrayList<>();
        expected.add(book1);
        expected.add(book2);
        expected.add(book3);
        return expected;
    }

    public static List<BookDtoWithoutCategoryIds> getBooksDtoWithNoCategories() {
        BookDtoWithoutCategoryIds book1 = new BookDtoWithoutCategoryIds();
        book1.setId(1L);
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("123-12-1234-123-1");
        book1.setPrice(new BigDecimal("11.0"));
        book1.setDescription("Description 1");
        book1.setCoverImage("images.com/image1.jpg");

        BookDtoWithoutCategoryIds book2 = new BookDtoWithoutCategoryIds();
        book2.setId(2L);
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setIsbn("123-12-1234-123-2");
        book2.setPrice(new BigDecimal("22.0"));
        book2.setDescription("Description 2");
        book2.setCoverImage("images.com/image2.jpg");

        BookDtoWithoutCategoryIds book3 = new BookDtoWithoutCategoryIds();
        book3.setId(3L);
        book3.setTitle("Book 3");
        book3.setAuthor("Author 3");
        book3.setIsbn("123-12-1234-123-3");
        book3.setPrice(new BigDecimal("33.0"));
        book3.setDescription("Description 3");
        book3.setCoverImage("images.com/image3.jpg");

        List<BookDtoWithoutCategoryIds> expected = new ArrayList<>();
        expected.add(book1);
        expected.add(book2);
        expected.add(book3);
        return expected;
    }

    public static CreateBookRequestDto getNewCreateBookRequestDto() {
        CreateBookRequestDto book = new CreateBookRequestDto();
        book.setTitle("Book 4");
        book.setAuthor("Author 4");
        book.setIsbn("123-12-1234-123-4");
        book.setPrice(new BigDecimal("44.0"));
        book.setDescription("Description 4");
        book.setCoverImage("images.com/image4.jpg");
        book.setCategoryIds(List.of(1L));
        return book;
    }

    public static BookDtoWithoutCategoryIds getBookDtoWithoutCategory() {
        BookDtoWithoutCategoryIds book1 = new BookDtoWithoutCategoryIds();
        book1.setId(1L);
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("123-12-1234-123-1");
        book1.setPrice(new BigDecimal("11.00"));
        book1.setDescription("Description 1");
        book1.setCoverImage("images.com/image1.jpg");
        return book1;
    }

    public static BookDto getBookDtoByRequestDto(Long id, CreateBookRequestDto requestDto) {
        BookDto book = new BookDto();
        book.setId(id);
        book.setTitle(requestDto.getTitle());
        book.setAuthor(requestDto.getAuthor());
        book.setIsbn(requestDto.getIsbn());
        book.setPrice(requestDto.getPrice());
        book.setDescription(requestDto.getDescription());
        book.setCoverImage(requestDto.getCoverImage());
        book.setCategoryIds(requestDto.getCategoryIds());
        return book;
    }

    public static Category getCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Category 1");
        category.setDescription("Category 1");
        return category;
    }

    public static Category getUnusedCategory() {
        Category category = new Category();
        category.setId(3L);
        category.setName("Category 3");
        category.setDescription("Category 3");
        return category;
    }

    public static Book getOneBook() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Title");
        book.setAuthor("Test Author");
        book.setIsbn("123-456-789");
        book.setPrice(BigDecimal.valueOf(19.99));
        return book;
    }

    public static BookDto getOneBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Test Title");
        bookDto.setAuthor("Test Author");
        bookDto.setIsbn("123-456-789");
        bookDto.setPrice(BigDecimal.valueOf(19.99));
        return bookDto;
    }

    public static CreateBookRequestDto getOneCreateBookRequestDto() {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("Test Title");
        createBookRequestDto.setAuthor("Test Author");
        createBookRequestDto.setIsbn("123-456-789");
        createBookRequestDto.setPrice(BigDecimal.valueOf(19.99));
        createBookRequestDto.setCategoryIds(Collections.singletonList(1L));
        return createBookRequestDto;
    }

    public static List<CategoryDto> getCategoriesDto() {
        CategoryDto category1 = new CategoryDto();
        category1.setId(1L);
        category1.setName("Category 1");
        category1.setDescription("Category 1");
        CategoryDto category2 = new CategoryDto();
        category2.setId(2L);
        category2.setName("Category 2");
        category2.setDescription("Category 2");

        List<CategoryDto> expected = new ArrayList<>();
        expected.add(category1);
        expected.add(category2);
        return expected;
    }

    public static CreateCategoryRequestDto getNewCreateCategoryRequestDto() {
        CreateCategoryRequestDto category = new CreateCategoryRequestDto();
        category.setName("Category 3");
        category.setDescription("Category 3");
        return category;
    }

    public static CategoryDto getCategoryDto() {
        CategoryDto category = new CategoryDto();
        category.setId(1L);
        category.setName("Category 1");
        category.setDescription("Category 1");
        return category;
    }

    public static CategoryDto getCategoryDtoByRequestDto(Long id, CreateCategoryRequestDto requestDto) {
        CategoryDto category = new CategoryDto();
        category.setId(id);
        category.setName(requestDto.getName());
        category.setDescription(requestDto.getDescription());
        return category;
    }
}
