package dev.vasyl.proj.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record PlaceOrderRequestDto(
        @Schema(description = "Shipping address",
                example = """
                        47 ANYVILLE RD
                        READING
                        BERKSHIRE
                        RG1 1AT
                        UNITED KINGDOM""")
        @NotEmpty(message = "Shipping address can`t be empty")
        String shippingAddress) {
}
