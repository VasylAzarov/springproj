package dev.vasyl.proj.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import dev.vasyl.proj.dto.book.BookDto;
import dev.vasyl.proj.dto.book.BookDtoWithoutCategoryIds;
import dev.vasyl.proj.dto.book.CreateBookRequestDto;
import dev.vasyl.proj.security.JwtUtil;
import dev.vasyl.proj.util.TestUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookControllerTests {
    protected static MockMvc mockMvc;
    private static final String CONTROLLER_ENDPOINT = "/books";
    private static final String DB_PATH_ADD_BOOKS_WITH_CATEGORIES = "database/book/add-books.sql";
    private static final String CLEAR_BOOKS_AND_CATEGORIES = "database/book/clear-books-and-categories.sql";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
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
    @DisplayName("Verify that book is created successfully when requestDto correct")
    void createBook_correctRequestDto_returnsCreatedBook() throws Exception {
        CreateBookRequestDto requestDto = TestUtil.getNewCreateBookRequestDto();
        BookDto expectedBook = TestUtil.getBookDtoByRequestDto(4L, requestDto);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post(CONTROLLER_ENDPOINT)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getAdminToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actualBook = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        assertNotNull(actualBook.getId());
        assertEquals(expectedBook.getTitle(), actualBook.getTitle());
        assertEquals(expectedBook.getAuthor(), actualBook.getAuthor());
        assertEquals(expectedBook.getIsbn(), actualBook.getIsbn());
    }

    @Test
    @DisplayName("Verify that all books is returned successfully")
    void getAll_threeBooksInDb_returnsAllBooksDtoWithoutCategoryId() throws Exception {
        List<BookDtoWithoutCategoryIds> expectedListOfBooks = TestUtil.getBooksDtoWithNoCategories();

        MvcResult result = mockMvc.perform(get(CONTROLLER_ENDPOINT)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getUserToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsByteArray());
        BookDtoWithoutCategoryIds[] actualListOfBooks =
                objectMapper.treeToValue(root.get("content"), BookDtoWithoutCategoryIds[].class);

        assertNotNull(actualListOfBooks);
        assertEquals(3, actualListOfBooks.length);
        assertEquals(expectedListOfBooks, Arrays.stream(actualListOfBooks).toList());
    }

    @Test
    @DisplayName("Verify that book is updated successfully when requestDto correct")
    void updateBookById_correctRequestDto_returnsUpdatedBook() throws Exception {
        Long bookId = 3L;
        CreateBookRequestDto requestDto = TestUtil.getNewCreateBookRequestDto();
        BookDto expectedBook = TestUtil.getBookDtoByRequestDto(bookId, requestDto);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put(CONTROLLER_ENDPOINT + "/" + bookId)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getAdminToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actualBook = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        assertNotNull(actualBook.getId());
        assertEquals(expectedBook.getTitle(), actualBook.getTitle());
        assertEquals(expectedBook.getAuthor(), actualBook.getAuthor());
        assertEquals(expectedBook.getIsbn(), actualBook.getIsbn());
    }

    @Test
    @DisplayName("Verify that book is returned successfully when id correct")
    void getBookById_correctId_returnsBookDtoWithNoCategories() throws Exception {
        long bookId = 1L;
        BookDtoWithoutCategoryIds expectedBook = TestUtil.getBookDtoWithoutCategory();

        MvcResult result = mockMvc.perform(get(CONTROLLER_ENDPOINT + "/" + bookId)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getUserToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDtoWithoutCategoryIds actualBook = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookDtoWithoutCategoryIds.class);

        assertNotNull(actualBook);
        assertEquals(expectedBook, actualBook);
    }

    @Test
    @DisplayName("Verify that book is deleted successfully when id correct")
    void deleteBook_correctId_returnsNoContentStatus() throws Exception {
        long bookId = 2L;

        mockMvc.perform(delete(CONTROLLER_ENDPOINT + "/" + bookId)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getAdminToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
        mockMvc.perform(get(CONTROLLER_ENDPOINT + "/" + bookId)
                        .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + getUserToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    public String getAdminToken() {
        return jwtUtil.generateToken("admin@admin.com");
    }

    public String getUserToken() {
        return jwtUtil.generateToken("user1@email.com");
    }
}
