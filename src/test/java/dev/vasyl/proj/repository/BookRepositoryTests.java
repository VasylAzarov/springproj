package dev.vasyl.proj.repository;

import dev.vasyl.proj.config.TestContainersConfig;
import dev.vasyl.proj.model.Book;
import dev.vasyl.proj.model.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.math.BigDecimal;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTests {

    private static final Book BOOK = new Book();
    private static final Category CATEGORY = new Category();

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeAll
    public static void setup() {
        CATEGORY.setName("category");
        BOOK.setTitle("Title");
        BOOK.setAuthor("Author");
        BOOK.setIsbn("someIsbn");
        BOOK.setPrice(BigDecimal.valueOf(1.1));
        BOOK.setCategories(Set.of(CATEGORY));
    }

    @AfterAll
    public static void stop() {
        TestContainersConfig.getMySQLContainer().stop();
    }

    @Test
    @DisplayName("Verify that a book exists for the specified ISBN")
    public void existsByIsbn_shouldReturnTrue_whenBookExist(){
        categoryRepository.save(CATEGORY);
        bookRepository.save(BOOK);
        Assertions.assertTrue(bookRepository.existsByIsbn(BOOK.getIsbn()));
    }

    @Test
    @DisplayName("Verify that a book not exists for the specified ISBN")
    public void existsByIsbn_shouldReturnFalse_whenBookNotExist(){
        final String notValidIsbn = "randomString";
        Assertions.assertFalse(bookRepository.existsByIsbn(notValidIsbn));
    }

    @Test
    @DisplayName("Verify that a book page has content when a book exists in the given category")
    public void findByCategoriesId_shouldReturnTrue_whenBookExists(){
        Page<Book> bookPage = bookRepository.findByCategoriesId(CATEGORY.getId(),
                PageRequest.of(0,1));
        Assertions.assertFalse(bookPage.hasContent());
    }
    @Test
    @DisplayName("Verify that a book page has no content when a book not exists in the given category")
    public void findByCategoriesId_shouldReturnFalse_whenBookNotExists(){
        bookRepository.delete(BOOK);
        Page<Book> bookPage = bookRepository.findByCategoriesId(CATEGORY.getId(),
                PageRequest.of(0,1));
        Assertions.assertFalse(bookPage.hasContent());
    }
}
