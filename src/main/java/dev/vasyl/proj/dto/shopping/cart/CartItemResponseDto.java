package dev.vasyl.proj.dto.shopping.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CartItemResponseDto {
    @Schema(description = "Cart item id",
            example = "1")
    private Long id;

    @Schema(description = "Book id",
            example = "23")
    private Long bookId;

    @Schema(description = "Book title",
            example = "Book title")
    private String bookTitle;

    @Schema(description = "Cart item йгфтешен id",
            example = "1")
    private int quantity;
}
