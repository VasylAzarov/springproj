package dev.vasyl.proj.dto.order;

import dev.vasyl.proj.model.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class OrderResponseDto {
    @Schema(description = "Order id",
            example = "1")
    private Long id;

    @Schema(description = "User id",
            example = "12")
    private Long userId;

    @Schema(description = "Order items page")
    private Set<OrderItemResponseDto> orderItems;

    @Schema(description = "Local date time",
            example = "2024-12-13T14:00:00")
    private LocalDateTime orderDate;

    @Schema(description = "Total price",
            example = "25.50")
    private BigDecimal total;

    @Schema(description = "Order status, can be:"
            + """
                        PENDING,
                        CONFIRMED,
                        PAID,
                        PROCESSING,
                        SHIPPED,
                        DELIVERED,
                        RETURNED,
                        REFUNDED,
                        ON_HOLD,
                        CANCELLED,
                        FAILED
                    """,
            example = "PENDING")
    private Order.Status status;
}
