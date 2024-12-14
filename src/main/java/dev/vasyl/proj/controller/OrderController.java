package dev.vasyl.proj.controller;

import dev.vasyl.proj.dto.order.OrderItemResponseDto;
import dev.vasyl.proj.dto.order.OrderResponseDto;
import dev.vasyl.proj.dto.order.PlaceOrderRequestDto;
import dev.vasyl.proj.dto.order.UpdateOrderStatusRequestDto;
import dev.vasyl.proj.model.User;
import dev.vasyl.proj.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
@Tag(name = "Order manager",
        description = "API for managing orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get  page of user order history",
            description = "Get page of user order. Available for User")
    public Page<OrderResponseDto> getAllOrders(@AuthenticationPrincipal User user,
                                               Pageable pageable) {
        return orderService.findAll(user, pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Place an order",
            description = "Remove items from cart and place new order with this items."
                    + " Available for User")
    public OrderResponseDto addItem(@AuthenticationPrincipal User user,
                                    @RequestBody
                                    @Valid PlaceOrderRequestDto placeOrderRequestDto) {
        return orderService.save(user, placeOrderRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get page of order items by orderId path",
            description = "Get page of order items by orderId path. Available for User")
    public Page<OrderItemResponseDto> getAllOrderItemsByOrderIdPath(@PathVariable Long orderId,
                                                                    Pageable pageable) {
        return orderService.findAllItemsByOrderId(orderId, pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get order item by itemId path",
            description = "Get order item by itemId path. Available for User")
    public OrderItemResponseDto getOrderItemsByOrderItemIdPath(@PathVariable Long itemId) {
        return orderService.findItemByItemId(itemId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Patch order item status by itemId path",
            description = "Patch order item status by itemId path. Available for User")
    public OrderResponseDto patchOrderStatus(@PathVariable Long id,
                                             @RequestBody @Valid UpdateOrderStatusRequestDto
                                                     updateOrderStatusRequestDto) {
        return orderService.updateStatusByItemId(id, updateOrderStatusRequestDto);
    }
}
