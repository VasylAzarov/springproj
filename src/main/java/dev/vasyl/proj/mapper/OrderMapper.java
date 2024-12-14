package dev.vasyl.proj.mapper;

import dev.vasyl.proj.config.MapperConfig;
import dev.vasyl.proj.dto.order.OrderItemResponseDto;
import dev.vasyl.proj.dto.order.OrderResponseDto;
import dev.vasyl.proj.model.Order;
import dev.vasyl.proj.model.OrderItem;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {

    @Mapping(target = "bookId", source = "book.id")
    OrderItemResponseDto toOrderItemResponseDto(OrderItem orderItem);

    @Mappings({
            @Mapping(target = "userId", source = "user.id"),
            @Mapping(target = "orderItems", source = "orderItems",
                    qualifiedByName = "toOrderItemsResponseDtoList")
    })
    OrderResponseDto toOrderResponseDto(Order order);

    default Page<OrderResponseDto> toOrderPageResponseDto(Page<Order> orderPage) {
        List<OrderResponseDto> dtoList = orderPage.getContent().stream()
                .map(this::toOrderResponseDto)
                .toList();
        return new PageImpl<>(dtoList, orderPage.getPageable(), orderPage.getTotalElements());
    }

    default Page<OrderItemResponseDto> toOrderItemPageResponseDto(Page<OrderItem> orderItemPage) {
        List<OrderItemResponseDto> dtoList = orderItemPage.getContent().stream()
                .map(this::toOrderItemResponseDto)
                .toList();
        return new PageImpl<>(dtoList, orderItemPage.getPageable(),
                orderItemPage.getTotalElements());
    }

    @Named("toOrderItemsResponseDtoList")
    default List<OrderItemResponseDto> toOrderItemsResponseDtoList(Set<OrderItem> orderItemSet) {
        return orderItemSet.stream()
                .map(this::toOrderItemResponseDto)
                .toList();
    }
}
