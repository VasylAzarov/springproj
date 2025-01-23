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
import dev.vasyl.proj.dto.category.CategoryDto;
import dev.vasyl.proj.dto.category.CreateCategoryRequestDto;
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

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryControllerTests {
    protected static MockMvc mockMvc;
    private static final String CONTROLLER_ENDPOINT = "/categories";
    private static final String DB_PATH_ADD_CATEGORIES = "database/category/add-categories.sql";
    private static final String CLEAR_CATEGORIES = "database/category/clear-categories.sql";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        executeSqlScript(dataSource, CLEAR_CATEGORIES);
    }

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void beforeEach(@Autowired DataSource dataSource) {
        executeSqlScript(dataSource, CLEAR_CATEGORIES);
        executeSqlScript(dataSource, DB_PATH_ADD_CATEGORIES);
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
    @DisplayName("Verify that all categories is returned successfully")
    void getAll_twoCategoriesInDb_returnsAllCategoriesDto() throws Exception {
        List<CategoryDto> categoriesDto = getCategoriesDto();
        MvcResult result = mockMvc.perform(get(CONTROLLER_ENDPOINT)
                        .header("Authorization", "Bearer " + getUserToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsByteArray());
        CategoryDto[] actualListOfBooks =
                objectMapper.treeToValue(root.get("content"), CategoryDto[].class);
        assertNotNull(actualListOfBooks);
        assertEquals(2, actualListOfBooks.length);
        assertEquals(categoriesDto, Arrays.stream(actualListOfBooks).toList());
    }

    @Test
    @DisplayName("Verify that category is returned successfully when id correct")
    void getCategoryById_correctId_returnsCategoryDto() throws Exception {
        long bookId = 1L;
        CategoryDto expectedCategory = getCategoryDto();
        MvcResult result = mockMvc.perform(get(CONTROLLER_ENDPOINT + "/" + bookId)
                        .header("Authorization", "Bearer " + getUserToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actualCategory = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), CategoryDto.class);
        assertNotNull(actualCategory);
        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    @DisplayName("Verify that category is created successfully when requestDto correct")
    void createCategory_correctRequestDto_returnsCreatedCategory() throws Exception {
        CreateCategoryRequestDto requestDto = getNewCreateCategoryRequestDto();
        CategoryDto expectedCategory = getCategoryDtoByRequestDto(3L, requestDto);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(post(CONTROLLER_ENDPOINT)
                        .header("Authorization", "Bearer " + getAdminToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();
        CategoryDto actualCategory = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        assertNotNull(actualCategory.getId());
        EqualsBuilder.reflectionEquals(expectedCategory, actualCategory, "id");
    }

    @Test
    @DisplayName("Verify that category is updated successfully when requestDto correct")
    void updateCategoryById_correctRequestDto_returnsUpdatedCategory() throws Exception {
        Long categoryId = 2L;
        CreateCategoryRequestDto requestDto = getNewCreateCategoryRequestDto();
        CategoryDto expectedCategory = getCategoryDtoByRequestDto(categoryId, requestDto);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        MvcResult result = mockMvc.perform(put(CONTROLLER_ENDPOINT + "/" + categoryId)
                        .header("Authorization", "Bearer " + getAdminToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();
        CategoryDto actualCategory = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        assertNotNull(actualCategory.getId());
        EqualsBuilder.reflectionEquals(expectedCategory, actualCategory, "id");
    }

    @Test
    @DisplayName("Verify that category is deleted successfully when id correct")
    void deleteCategoryById_correctId_returnsNoContentStatus() throws Exception {
        long categoryId = 2L;
        mockMvc.perform(delete(CONTROLLER_ENDPOINT + "/" + categoryId)
                        .header("Authorization", "Bearer " + getAdminToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
        mockMvc.perform(get(CONTROLLER_ENDPOINT + "/" + categoryId)
                        .header("Authorization", "Bearer " + getUserToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    private List<CategoryDto> getCategoriesDto() {
        List<CategoryDto> expected = new ArrayList<>();
        CategoryDto category1 = new CategoryDto();
        category1.setId(1L);
        category1.setName("Category 1");
        category1.setDescription("Category 1");
        CategoryDto category2 = new CategoryDto();
        category2.setId(2L);
        category2.setName("Category 2");
        category2.setDescription("Category 2");

        expected.add(category1);
        expected.add(category2);

        return expected;
    }

    private CreateCategoryRequestDto getNewCreateCategoryRequestDto() {
        CreateCategoryRequestDto category = new CreateCategoryRequestDto();
        category.setName("Category 3");
        category.setDescription("Category 3");
        return category;
    }

    private CategoryDto getCategoryDto() {
        CategoryDto category = new CategoryDto();
        category.setId(1L);
        category.setName("Category 1");
        category.setDescription("Category 1");
        return category;
    }

    private CategoryDto getCategoryDtoByRequestDto(Long id, CreateCategoryRequestDto requestDto) {
        CategoryDto category = new CategoryDto();
        category.setId(id);
        category.setName(requestDto.getName());
        category.setDescription(requestDto.getDescription());
        return category;
    }

    private String getAdminToken() {
        return jwtUtil.generateToken("admin@admin.com");
    }

    private String getUserToken() {
        return jwtUtil.generateToken("user1@email.com");
    }

}
