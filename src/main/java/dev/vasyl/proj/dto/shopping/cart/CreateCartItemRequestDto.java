package dev.vasyl.proj.dto.shopping.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCartItemRequestDto(
        @Schema(description = "Book id",
                example = "2")
        @NotNull(message = "id is empty")
        Long bookId,
        @Schema(description = "Cart item quantity",
                example = "1")
        @Positive
        int quantity) {
}
