package dev.vasyl.proj.mapper;

import dev.vasyl.proj.config.MapperConfig;
import dev.vasyl.proj.dto.order.OrderItemResponseDto;
import dev.vasyl.proj.dto.order.OrderResponseDto;
import dev.vasyl.proj.model.Order;
import dev.vasyl.proj.model.OrderItem;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

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
        return orderPage.map(this::toOrderResponseDto);
    }

    default Page<OrderItemResponseDto> toOrderItemPageResponseDto(Page<OrderItem> orderItemPage) {
        return orderItemPage.map(this::toOrderItemResponseDto);
    }

    @Named("toOrderItemsResponseDtoList")
    default Set<OrderItemResponseDto> toOrderItemsResponseDtoList(Set<OrderItem> orderItemSet) {
        return orderItemSet.stream()
                .map(this::toOrderItemResponseDto)
                .collect(Collectors.toSet());
    }
}
