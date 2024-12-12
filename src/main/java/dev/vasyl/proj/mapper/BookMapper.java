package dev.vasyl.proj.mapper;

import dev.vasyl.proj.config.MapperConfig;
import dev.vasyl.proj.dto.book.BookDto;
import dev.vasyl.proj.dto.book.BookDtoWithoutCategoryIds;
import dev.vasyl.proj.dto.book.CreateBookRequestDto;
import dev.vasyl.proj.model.Book;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(config = MapperConfig.class, uses = CategoryMapper.class)
public interface BookMapper {

    @Mapping(target = "categoryIds", source = "categories", qualifiedByName = "toIdList")
    BookDto toDto(Book book);

    @Mapping(target = "categories", source = "categoryIds", qualifiedByName = "toModelList")
    Book toModel(CreateBookRequestDto bookDto);

    @Mapping(target = "categories", expression =
            "java(categoryMapper.toModelList(requestDto.getCategoryIds()))")
    void updateBookFromDto(CreateBookRequestDto requestDto, @MappingTarget Book entity);

    BookDtoWithoutCategoryIds toBookDtoWithoutCategoryIdsPage(Book book);

    default Page<BookDtoWithoutCategoryIds> toBookDtoWithoutCategoryIdsPage(Page<Book> bookPage) {
        List<BookDtoWithoutCategoryIds> dtoList = bookPage.getContent().stream()
                .map(this::toBookDtoWithoutCategoryIdsPage)
                .toList();
        return new PageImpl<>(dtoList, bookPage.getPageable(), bookPage.getTotalElements());
    }

    default Page<BookDto> toBookDtoPage(Page<Book> bookPage) {
        List<BookDto> dtoList = bookPage.getContent().stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(dtoList, bookPage.getPageable(), bookPage.getTotalElements());
    }
}
