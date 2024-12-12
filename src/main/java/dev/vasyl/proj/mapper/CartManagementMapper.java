package dev.vasyl.proj.mapper;

import dev.vasyl.proj.config.MapperConfig;
import dev.vasyl.proj.dto.shopping.cart.CartItemDto;
import dev.vasyl.proj.dto.shopping.cart.CartResponseDto;
import dev.vasyl.proj.model.CartItem;
import dev.vasyl.proj.model.ShoppingCart;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface CartManagementMapper {

    @Mappings({
            @Mapping(target = "bookId", source = "book.id"),
            @Mapping(target = "bookTitle", source = "book.title")})
    CartItemDto toCartItemDto(CartItem cartItem);

    @Mappings({
            @Mapping(target = "cartItems", source = "cartItems",
                    qualifiedByName = "cartItemsSetToFirstPageCartItemDto"),
            @Mapping(target = "userId", source = "user.id"),
    })
    CartResponseDto toCartDto(ShoppingCart shoppingCartByUserId);

    @Named("cartItemsSetToFirstPageCartItemDto")
    default Page<CartItemDto> cartItemsSetToFirstPageCartItemDto(Set<CartItem> cartItems) {
        List<CartItemDto> dtoList = cartItems.stream()
                .map(this::toCartItemDto)
                .toList();
        return dtoList.isEmpty()
                ? Page.empty()
                : new PageImpl<>(dtoList, PageRequest.of(0, dtoList.size()), dtoList.size());
    }

    @Named("toCartItemsDtoPage")
    default Page<CartItemDto> toCartItemsDtoPage(Page<CartItem> cartItemPage) {
        List<CartItemDto> dtoList = cartItemPage.getContent().stream()
                .map(this::toCartItemDto)
                .toList();
        return new PageImpl<>(dtoList, cartItemPage.getPageable(),
                cartItemPage.getTotalElements());
    }
}
