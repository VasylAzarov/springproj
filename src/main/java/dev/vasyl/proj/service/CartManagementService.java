package dev.vasyl.proj.service;

import dev.vasyl.proj.dto.shopping.cart.CartItemDto;
import dev.vasyl.proj.dto.shopping.cart.CartResponseDto;
import dev.vasyl.proj.dto.shopping.cart.CreateCartItemRequestDto;
import dev.vasyl.proj.dto.shopping.cart.UpdateCartItemRequestDto;
import dev.vasyl.proj.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CartManagementService {

    CartResponseDto getCart(User user);

    Page<CartItemDto> findAll(User user, Pageable pageable);

    CartItemDto save(User user, CreateCartItemRequestDto createCartItemRequestDto);

    CartItemDto update(Long id, UpdateCartItemRequestDto updateCartItemDto);

    void deleteById(Long id);
}
