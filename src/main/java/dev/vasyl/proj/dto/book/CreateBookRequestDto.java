package dev.vasyl.proj.dto.book;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank
    @Schema(description = "Title of the book",
            example = "The Great Gatsby")
    private String title;
    @NotBlank
    @Schema(description = "Author of the book",
            example = "F. Scott Fitzgerald")
    private String author;
    @NotBlank
    @Schema(description = " International Standard Book Number (ISBN)",
            example = "978-3-16-148410-0")
    private String isbn;
    @NotNull
    @Min(0)
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
