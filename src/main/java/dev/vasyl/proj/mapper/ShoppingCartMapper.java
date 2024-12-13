package dev.vasyl.proj.mapper;

import dev.vasyl.proj.config.MapperConfig;
import dev.vasyl.proj.dto.shopping.cart.CartItemResponseDto;
import dev.vasyl.proj.dto.shopping.cart.CartResponseDto;
import dev.vasyl.proj.model.CartItem;
import dev.vasyl.proj.model.ShoppingCart;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface ShoppingCartMapper {

    @Mappings({
            @Mapping(target = "bookId", source = "book.id"),
            @Mapping(target = "bookTitle", source = "book.title")})
    CartItemResponseDto toCartItemDto(CartItem cartItem);

    @Mappings({
            @Mapping(target = "cartItems", source = "cartItems",
                    qualifiedByName = "cartItemsSetToFirstPageCartItemDto"),
            @Mapping(target = "userId", source = "user.id"),
    })
    CartResponseDto toCartDto(ShoppingCart shoppingCartByUserId);

    @Named("cartItemsSetToFirstPageCartItemDto")
    default Page<CartItemResponseDto> cartItemsSetToFirstPageCartItemDto(Set<CartItem> cartItems) {
        return new PageImpl<>(
                cartItems.stream()
                        .map(this::toCartItemDto)
                        .toList(),
                PageRequest.of(0, Math.max(cartItems.size(), 1)),
                cartItems.size()
        );
    }

    @Named("toCartItemsDtoPage")
    default Page<CartItemResponseDto> toCartItemsDtoPage(Page<CartItem> cartItemPage) {
        return cartItemPage.map(this::toCartItemDto);
    }
}
