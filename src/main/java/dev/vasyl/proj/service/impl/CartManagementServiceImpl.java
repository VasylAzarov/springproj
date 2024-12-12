package dev.vasyl.proj.service.impl;

import dev.vasyl.proj.dto.shopping.cart.CartItemDto;
import dev.vasyl.proj.dto.shopping.cart.CartItemOperation;
import dev.vasyl.proj.dto.shopping.cart.CartResponseDto;
import dev.vasyl.proj.dto.shopping.cart.CreateCartItemRequestDto;
import dev.vasyl.proj.dto.shopping.cart.UpdateCartItemRequestDto;
import dev.vasyl.proj.exception.EntityNotFoundException;
import dev.vasyl.proj.mapper.CartManagementMapper;
import dev.vasyl.proj.model.Book;
import dev.vasyl.proj.model.CartItem;
import dev.vasyl.proj.model.ShoppingCart;
import dev.vasyl.proj.model.User;
import dev.vasyl.proj.repository.BookRepository;
import dev.vasyl.proj.repository.CartItemRepository;
import dev.vasyl.proj.repository.ShoppingCartRepository;
import dev.vasyl.proj.service.CartManagementService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartManagementServiceImpl implements CartManagementService {
    private static final int DEFAULT_QUANTITY = 1;

    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final CartManagementMapper cartManagementMapper;

    @Override
    public CartResponseDto getCart(User user) {
        return cartManagementMapper.toCartDto(getShoppingCartByUserId(user.getId()));
    }

    @Override
    public Page<CartItemDto> findAll(User user, Pageable pageable) {
        Long cartId = getShoppingCartByUserId(user.getId()).getId();
        Page<CartItem> cartItemPage = cartItemRepository.findAllByShoppingCartId(cartId, pageable);
        return cartManagementMapper.toCartItemsDtoPage(cartItemPage);
    }

    @Override
    public CartItemDto save(User user, CreateCartItemRequestDto createCartItemRequestDto) {
        Book book = getBookById(createCartItemRequestDto.bookId());
        ShoppingCart shoppingCart = getShoppingCartByUserId(user.getId());
        CartItem cartItem = getCartItemIfExist(shoppingCart, book);
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + DEFAULT_QUANTITY);
        } else {
            cartItem = new CartItem();
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setBook(book);
            cartItem.setQuantity(DEFAULT_QUANTITY);
        }
        return cartManagementMapper.toCartItemDto(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemDto update(Long id, UpdateCartItemRequestDto updateCartItemDto) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can`t find CartItem entity by id: " + id));
        int calculatedQuantity = getCalculatedQuantity(cartItem, updateCartItemDto);
        cartItem.setQuantity(calculatedQuantity);
        return cartManagementMapper.toCartItemDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }

    private ShoppingCart getShoppingCartByUserId(Long userId) {
        return shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can`t find ShoppingCart entity by user id: " + userId));
    }

    private Book getBookById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can`t find Book entity by book id: " + bookId));
    }

    private CartItem getCartItemIfExist(ShoppingCart shoppingCart, Book book) {
        Long bookId = book.getId();
        Set<CartItem> cartItemSet = shoppingCart.getCartItems();
        return cartItemSet.stream()
                .filter(cartItem -> cartItem.getBook().getId().equals(bookId))
                .findFirst()
                .orElse(null);
    }

    private int getCalculatedQuantity(CartItem cartItem,
                                      UpdateCartItemRequestDto updateCartItemDto) {
        int currentQuantity = cartItem.getQuantity();
        int change = updateCartItemDto.quantity();
        if (updateCartItemDto.operation() == CartItemOperation.DECREASE
                && currentQuantity < change) {
            return 0;
        }
        return updateCartItemDto.operation() == CartItemOperation.DECREASE
                ? currentQuantity - change
                : currentQuantity + change;
    }
}
