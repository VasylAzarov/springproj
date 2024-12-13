package dev.vasyl.proj.service;

import dev.vasyl.proj.dto.shopping.cart.CartResponseDto;
import dev.vasyl.proj.dto.shopping.cart.CreateCartItemRequestDto;
import dev.vasyl.proj.dto.shopping.cart.UpdateCartItemRequestDto;
import dev.vasyl.proj.model.User;

public interface ShoppingCartService {

    CartResponseDto getCart(User user);

    CartResponseDto save(User user, CreateCartItemRequestDto createCartItemRequestDto);

    CartResponseDto update(Long id, UpdateCartItemRequestDto updateCartItemDto);

    void deleteById(Long id);

    void createShoppingCartForUser(User user);
}
