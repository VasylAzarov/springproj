package dev.vasyl.proj.repository;

import static dev.vasyl.proj.config.CustomMySqlContainer.getInstance;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.data.domain.PageRequest.of;

import dev.vasyl.proj.model.Book;
import dev.vasyl.proj.util.TestUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import javax.sql.DataSource;
import java.sql.Connection;

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
        assertTrue(bookRepository.existsByIsbn(TestUtil.getListOfBooks().get(0).getIsbn()));
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
        Page<Book> bookPage = bookRepository.findByCategoriesId(TestUtil.getCategory().getId(),
                of(0, 1));

        assertTrue(bookPage.hasContent());
    }

    @Test
    @DisplayName("Verify that a book page has no content when a book not exists in the given category")
    @Sql(scripts = "classpath:database/book/clear-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void findByCategoriesId_shouldReturnFalse_whenBookNotExists() {
        Page<Book> bookPage = bookRepository.findByCategoriesId(TestUtil.getUnusedCategory().getId(),
                of(0, 1));

        assertFalse(bookPage.hasContent());
    }
}
