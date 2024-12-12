package dev.vasyl.proj.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryRequestDto {
    @NotBlank
    @Schema(description = "Category name",
            example = "Fantasy")
    private String name;

    @Schema(description = "Category description",
            example = "Is a genre of speculative fiction which involves...")
    private String description;
}
