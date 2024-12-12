package dev.vasyl.proj.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CategoryDto {
    @Schema(description = "Category id",
            example = "1")
    private Long id;
    @Schema(description = "Category name",
            example = "Fantasy")
    private String name;

    @Schema(description = "Category description",
            example = "Is a genre of speculative fiction which involves...")
    private String description;
}
