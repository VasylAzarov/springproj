package dev.vasyl.proj.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookDto {
    @Schema(description = "Book id",
            example = "1")
    private Long id;
    @Schema(description = "Title of the book",
            example = "The Great Gatsby")
    private String title;
    @Schema(description = "Author of the book",
            example = "F. Scott Fitzgerald")
    private String author;
    @Schema(description = " International Standard Book Number (ISBN)",
            example = "978-3-16-148410-0")
    private String isbn;
    @Schema(description = "Price of the book",
            example = "19.99")
    private BigDecimal price;
    @Schema(description = "Description of the book",
            example = "Grate book about...")
    private String description;
    @Schema(description = "Cover image of the book",
            example = "https://cover-book-image.com/12")
    private String coverImage;
}
