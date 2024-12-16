package dev.vasyl.proj.dto.order;

import dev.vasyl.proj.model.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequestDto(
        @Schema(description = """
                Status can be:
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
                    FAILED""",
                example = "PENDING")
        @NotNull(message = "Status can`t be null")
        Order.Status status) {
}
