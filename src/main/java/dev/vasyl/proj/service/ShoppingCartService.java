package dev.vasyl.proj.service;

import dev.vasyl.proj.dto.shopping.cart.CartItemResponseDto;
import dev.vasyl.proj.dto.shopping.cart.CartResponseDto;
import dev.vasyl.proj.dto.shopping.cart.CreateCartItemRequestDto;
import dev.vasyl.proj.dto.shopping.cart.UpdateCartItemRequestDto;
import dev.vasyl.proj.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShoppingCartService {

    CartResponseDto getCart(User user);

    Page<CartItemResponseDto> findAll(User user, Pageable pageable);

    CartItemResponseDto save(User user, CreateCartItemRequestDto createCartItemRequestDto);

    CartItemResponseDto update(Long id, UpdateCartItemRequestDto updateCartItemDto);

    void deleteById(Long id);

    void createShoppingCartForUser(User user);
}
