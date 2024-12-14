package dev.vasyl.proj.service;

import dev.vasyl.proj.dto.order.OrderItemResponseDto;
import dev.vasyl.proj.dto.order.OrderResponseDto;
import dev.vasyl.proj.dto.order.PlaceOrderRequestDto;
import dev.vasyl.proj.dto.order.UpdateOrderStatusRequestDto;
import dev.vasyl.proj.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    Page<OrderResponseDto> findAll(User user, Pageable pageable);

    Page<OrderItemResponseDto> findAllItemsByOrderId(Long orderId, Pageable pageable);

    OrderItemResponseDto findItemByItemId(Long orderItemId);

    OrderResponseDto save(User user,
                          PlaceOrderRequestDto placeOrderRequestDto);

    OrderResponseDto updateStatusByItemId(Long itemId,
                                          UpdateOrderStatusRequestDto updateOrderStatusRequestDto);
}
