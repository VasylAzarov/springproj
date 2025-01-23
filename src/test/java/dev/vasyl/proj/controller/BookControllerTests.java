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

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;

import dev.vasyl.proj.dto.book.BookDto;
import dev.vasyl.proj.dto.book.BookDtoWithoutCategoryIds;
import dev.vasyl.proj.dto.book.CreateBookRequestDto;
import dev.vasyl.proj.security.JwtUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
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
        CreateBookRequestDto requestDto = getNewCreateBookRequestDto();
        BookDto expectedBook = getBookDtoByRequestDto(4L, requestDto);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(post(CONTROLLER_ENDPOINT)
                        .header("Authorization", "Bearer " + getAdminToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actualBook = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        assertNotNull(actualBook.getId());
        EqualsBuilder.reflectionEquals(expectedBook, actualBook, "id");
    }

    @Test
    @DisplayName("Verify that all books is returned successfully")
    void getAll_threeBooksInDb_returnsAllBooksDtoWithoutCategoryId() throws Exception {
        List<BookDtoWithoutCategoryIds> expectedListOfBooks = getBooksDtoWithNoCategories();
        MvcResult result = mockMvc.perform(get(CONTROLLER_ENDPOINT)
                        .header("Authorization", "Bearer " + getUserToken())
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
        CreateBookRequestDto requestDto = getNewCreateBookRequestDto();
        BookDto expectedBook = getBookDtoByRequestDto(bookId, requestDto);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(put(CONTROLLER_ENDPOINT + "/" + bookId)
                        .header("Authorization", "Bearer " + getAdminToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actualBook = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        assertNotNull(actualBook.getId());
        EqualsBuilder.reflectionEquals(expectedBook, actualBook, "id");
    }

    @Test
    @DisplayName("Verify that book is returned successfully when id correct")
    void getBookById_correctId_returnsBookDtoWithNoCategories() throws Exception {
        long bookId = 1L;
        BookDtoWithoutCategoryIds expectedBook = getBookDtoWithoutCategory();
        MvcResult result = mockMvc.perform(get(CONTROLLER_ENDPOINT + "/" + bookId)
                        .header("Authorization", "Bearer " + getUserToken())
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
                        .header("Authorization", "Bearer " + getAdminToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
        mockMvc.perform(get(CONTROLLER_ENDPOINT + "/" + bookId)
                        .header("Authorization", "Bearer " + getUserToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private List<BookDtoWithoutCategoryIds> getBooksDtoWithNoCategories() {
        List<BookDtoWithoutCategoryIds> expected = new ArrayList<>();
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

        expected.add(book1);
        expected.add(book2);
        expected.add(book3);

        return expected;
    }

    private CreateBookRequestDto getNewCreateBookRequestDto() {
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

    private BookDtoWithoutCategoryIds getBookDtoWithoutCategory() {
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

    private BookDto getBookDtoByRequestDto(Long id, CreateBookRequestDto requestDto) {
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


    private String getAdminToken() {
        return jwtUtil.generateToken("admin@admin.com");
    }

    private String getUserToken() {
        return jwtUtil.generateToken("user1@email.com");
    }

}
