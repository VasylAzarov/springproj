package dev.vasyl.proj.service.impl;

import dev.vasyl.proj.dto.shopping.cart.CartItemOperation;
import dev.vasyl.proj.dto.shopping.cart.CartResponseDto;
import dev.vasyl.proj.dto.shopping.cart.CreateCartItemRequestDto;
import dev.vasyl.proj.dto.shopping.cart.UpdateCartItemRequestDto;
import dev.vasyl.proj.exception.EntityNotFoundException;
import dev.vasyl.proj.exception.InsufficientItemsException;
import dev.vasyl.proj.mapper.ShoppingCartMapper;
import dev.vasyl.proj.model.Book;
import dev.vasyl.proj.model.CartItem;
import dev.vasyl.proj.model.ShoppingCart;
import dev.vasyl.proj.model.User;
import dev.vasyl.proj.repository.BookRepository;
import dev.vasyl.proj.repository.CartItemRepository;
import dev.vasyl.proj.repository.ShoppingCartRepository;
import dev.vasyl.proj.service.ShoppingCartService;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private static final int DEFAULT_QUANTITY = 1;

    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public CartResponseDto getCart(User user) {
        return shoppingCartMapper.toCartDto(getShoppingCartByUserId(user.getId()));
    }

    @Override
    public CartResponseDto save(User user, CreateCartItemRequestDto createCartItemRequestDto) {
        Book book = getBookById(createCartItemRequestDto.bookId());
        ShoppingCart shoppingCart = getShoppingCartByUserId(user.getId());
        Optional<CartItem> optionalCartItem = getCartItemIfExist(shoppingCart, book);
        CartItem cartItem;
        if (optionalCartItem.isPresent()) {
            cartItem = optionalCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + createCartItemRequestDto.quantity());
        } else {
            cartItem = new CartItem();
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setBook(book);
            cartItem.setQuantity(createCartItemRequestDto.quantity());
        }
        cartItemRepository.save(cartItem);
        shoppingCart = getShoppingCartByUserId(user.getId());
        return shoppingCartMapper.toCartDto(shoppingCart);
    }

    @Override
    public CartResponseDto update(Long id, UpdateCartItemRequestDto updateCartItemDto) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can`t find CartItem entity by id: " + id));
        int calculatedQuantity = getCalculatedQuantity(cartItem, updateCartItemDto);
        cartItem.setQuantity(calculatedQuantity);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toCartDto(cartItem.getShoppingCart());
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public void createShoppingCartForUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
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

    private Optional<CartItem> getCartItemIfExist(ShoppingCart shoppingCart, Book book) {
        Long bookId = book.getId();
        Set<CartItem> cartItemSet = shoppingCart.getCartItems();
        return cartItemSet.stream()
                .filter(cartItem -> cartItem.getBook().getId().equals(bookId))
                .findFirst();
    }

    private int getCalculatedQuantity(CartItem cartItem,
                                      UpdateCartItemRequestDto updateCartItemDto) {
        int currentQuantity = cartItem.getQuantity();
        int change = updateCartItemDto.quantity();
        if (updateCartItemDto.operation() == CartItemOperation.DECREASE
                && currentQuantity < change) {
            throw new InsufficientItemsException("Can`t decrease if decrease quantity["
                    + change + "] bigger then item quantity[" + currentQuantity + "]");
        }
        return updateCartItemDto.operation() == CartItemOperation.DECREASE
                ? currentQuantity - change
                : currentQuantity + change;
    }
}
