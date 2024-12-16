package dev.vasyl.proj.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OrderItemResponseDto {
    @Schema(description = "Order item id",
            example = "1")
    private Long id;

    @Schema(description = "Book id",
            example = "12")
    private Long bookId;

    @Schema(description = "Quantity",
            example = "4")
    private int quantity;
}
