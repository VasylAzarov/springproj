package dev.vasyl.proj.mapper;

import dev.vasyl.proj.config.MapperConfig;
import dev.vasyl.proj.dto.book.BookDto;
import dev.vasyl.proj.dto.book.CreateBookRequestDto;
import dev.vasyl.proj.model.Book;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    Book toModel(CreateBookRequestDto createBookRequestDto);

    void updateBookFromDto(CreateBookRequestDto requestDto, @MappingTarget Book entity);

    BookDto toDto(Book book);

    default Page<BookDto> toBookDtoPage(Page<Book> bookPage) {
        List<BookDto> dtoList = bookPage.getContent().stream()
                .map(this::toDto)
                .toList();
        return new PageImpl<>(dtoList, bookPage.getPageable(), bookPage.getTotalElements());
    }
}
