package dev.vasyl.proj.repository;

import static dev.vasyl.proj.config.CustomMySqlContainer.getInstance;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.data.domain.PageRequest.of;

import dev.vasyl.proj.model.Book;
import dev.vasyl.proj.model.Category;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTests {
    private static final String DB_PATH_ADD_BOOKS_WITH_CATEGORIES = "database/book/add-books.sql";
    private static final String CLEAR_BOOKS_AND_CATEGORIES = "database/book/clear-books-and-categories.sql";

    @Autowired
    private BookRepository bookRepository;

    @AfterAll
    public static void stop(@Autowired DataSource dataSource) {
        getInstance().stop();
        executeSqlScript(dataSource, CLEAR_BOOKS_AND_CATEGORIES);
    }

    @BeforeEach
    void beforeEach(@Autowired DataSource dataSource) {
        executeSqlScript(dataSource, CLEAR_BOOKS_AND_CATEGORIES);
        executeSqlScript(dataSource, DB_PATH_ADD_BOOKS_WITH_CATEGORIES);
    }

    @SneakyThrows
    static void executeSqlScript(DataSource dataSource, String dbPath) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(dbPath));
        }
    }

    @Test
    @DisplayName("Verify that a book exists for the specified ISBN")
    public void existsByIsbn_shouldReturnTrue_whenBookExist() {
        assertTrue(bookRepository.existsByIsbn(getListOfBooks().get(0).getIsbn()));
    }

    @Test
    @DisplayName("Verify that a book not exists for the specified ISBN")
    public void existsByIsbn_shouldReturnFalse_whenBookNotExist() {
        final String notValidIsbn = "invalid string";

        assertFalse(bookRepository.existsByIsbn(notValidIsbn));
    }

    @Test
    @DisplayName("Verify that a book page has content when a book exists in the given category")
    public void findByCategoriesId_shouldReturnTrue_whenBookExists() {
        Page<Book> bookPage = bookRepository.findByCategoriesId(getCategory().getId(),
                of(0, 1));

        assertTrue(bookPage.hasContent());
    }

    @Test
    @DisplayName("Verify that a book page has no content when a book not exists in the given category")
    @Sql(scripts = "classpath:database/book/clear-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findByCategoriesId_shouldReturnFalse_whenBookNotExists() {
        Page<Book> bookPage = bookRepository.findByCategoriesId(getUnusedCategory().getId(),
                of(0, 1));

        assertFalse(bookPage.hasContent());
    }

    private List<Book> getListOfBooks() {
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

    private Category getCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Category 1");
        category.setDescription("Category 1");
        return category;
    }

    private Category getUnusedCategory() {
        Category category = new Category();
        category.setId(3L);
        category.setName("Category 3");
        category.setDescription("Category 3");
        return category;
    }
}
