package dev.vasyl.proj.dto.shopping.cart;

import dev.vasyl.proj.dto.book.BookDtoWithoutCategoryIds;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CartItemDto {
    @Schema(description = "Cart item id",
            example = "1")
    private Long id;
    @Schema(description = "Book data",
            example = """
                    {
                    "title": "title2",
                    "author": "bookAuthor2",
                    "isbn": "bookSomeIsbn22",
                    "price" : 22.2,
                    "description": "bookDescription2",
                    "coverImage": "bookCoverImage2",
                    }""")
    private BookDtoWithoutCategoryIds bookDto;

    @Schema(description = "Cart item йгфтешен id",
            example = "1")
    private int quantity;
}
