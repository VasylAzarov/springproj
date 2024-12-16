package dev.vasyl.proj.service.impl;

import dev.vasyl.proj.dto.order.OrderItemResponseDto;
import dev.vasyl.proj.dto.order.OrderResponseDto;
import dev.vasyl.proj.dto.order.PlaceOrderRequestDto;
import dev.vasyl.proj.dto.order.UpdateOrderStatusRequestDto;
import dev.vasyl.proj.exception.EntityNotFoundException;
import dev.vasyl.proj.exception.OrderProcessingException;
import dev.vasyl.proj.mapper.OrderMapper;
import dev.vasyl.proj.model.CartItem;
import dev.vasyl.proj.model.Order;
import dev.vasyl.proj.model.OrderItem;
import dev.vasyl.proj.model.ShoppingCart;
import dev.vasyl.proj.model.User;
import dev.vasyl.proj.repository.OrderItemRepository;
import dev.vasyl.proj.repository.OrderRepository;
import dev.vasyl.proj.repository.ShoppingCartRepository;
import dev.vasyl.proj.service.OrderService;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;

    @Override
    public Page<OrderResponseDto> findAll(User user, Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAllByUserId(user.getId(), pageable);
        return orderMapper.toOrderPageResponseDto(orderPage);
    }

    @Override
    public Page<OrderItemResponseDto> findAllItemsByOrderId(Long orderId, Pageable pageable) {
        Page<OrderItem> orderItemPage = orderItemRepository.findAllByOrderId(orderId, pageable);
        return orderMapper.toOrderItemPageResponseDto(orderItemPage);
    }

    @Override
    public OrderItemResponseDto findItemByItemId(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can`t find Order Item entity by id: " + orderItemId));
        return orderMapper.toOrderItemResponseDto(orderItem);
    }

    @Override
    public OrderResponseDto save(User user,
                                 PlaceOrderRequestDto placeOrderRequestDto) {
        ShoppingCart shoppingCart = getShoppingCartByUserId(user.getId());
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new OrderProcessingException("Can`t create order with empty cart! user id:"
                    + user.getId());
        }
        Order order = new Order();
        Set<OrderItem> orderItems = convertToOrderItemSet(shoppingCart.getCartItems(), order);
        order.setUser(user);
        order.setStatus(Order.Status.PENDING);
        order.setOrderItems(orderItems);
        order.setTotal(calculateTotalPriceByOrderItems(orderItems));
        order.setShippingAddress(placeOrderRequestDto.shippingAddress());
        orderRepository.save(order);
        shoppingCart.getCartItems().forEach(cartItem -> cartItem.setDeleted(true));
        shoppingCartRepository.save(shoppingCart);
        return orderMapper.toOrderResponseDto(order);
    }

    @Override
    public OrderResponseDto updateStatusByItemId(Long orderId,
                                                 UpdateOrderStatusRequestDto
                                                         updateOrderStatusRequestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can`t find Order entity by id: " + orderId));
        order.setStatus(updateOrderStatusRequestDto.status());
        orderRepository.save(order);

        return orderMapper.toOrderResponseDto(order);
    }

    private ShoppingCart getShoppingCartByUserId(Long userId) {
        return shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can`t find Shopping cart entity by user id: " + userId));
    }

    private Set<OrderItem> convertToOrderItemSet(Set<CartItem> cartItems, Order order) {
        return cartItems.stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setPrice(cartItem.getBook().getPrice());
                    orderItem.setQuantity(cartItem.getQuantity());
                    return orderItem;
                })
                .collect(Collectors.toSet());
    }

    private BigDecimal calculateTotalPriceByOrderItems(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem ->
                        orderItem.getBook()
                                .getPrice()
                                .multiply(BigDecimal
                                        .valueOf(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
