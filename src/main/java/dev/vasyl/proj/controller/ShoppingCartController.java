package dev.vasyl.proj.controller;

import dev.vasyl.proj.dto.shopping.cart.CartItemDto;
import dev.vasyl.proj.dto.shopping.cart.CartResponseDto;
import dev.vasyl.proj.dto.shopping.cart.CreateCartItemRequestDto;
import dev.vasyl.proj.dto.shopping.cart.UpdateCartItemRequestDto;
import dev.vasyl.proj.model.User;
import dev.vasyl.proj.service.CartManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
@Tag(name = "Cart manager",
        description = "API for managing cart")
public class ShoppingCartController {
    private final CartManagementService cartManagementService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get user cart",
            description = "Get user cart. Available for User")
    public CartResponseDto getCart(@AuthenticationPrincipal User user) {
        return cartManagementService.getCart(user);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add item to user cart",
            description = "Add item to user cart. Available for User")
    public CartItemDto addItem(@AuthenticationPrincipal User user,
                               @RequestBody
                               @Valid CreateCartItemRequestDto createCartItemRequestDto) {
        return cartManagementService.save(user, createCartItemRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/items/{cartItemId}")
    @Operation(summary = "Update item quantity in user cart",
            description = "Update item quantity in user cart. Available for User")
    public CartItemDto updateItem(@PathVariable Long cartItemId,
                                  @RequestBody @Valid UpdateCartItemRequestDto cartItemRequestDto) {
        return cartManagementService.update(cartItemId, cartItemRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete item from user cart",
            description = "Delete item from user cart. Available for User")
    public void deleteItem(@PathVariable Long cartItemId) {
        cartManagementService.deleteById(cartItemId);
    }
}
