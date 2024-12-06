package dev.vasyl.proj.mapper;

import dev.vasyl.proj.config.MapperConfig;
import dev.vasyl.proj.dto.BookDto;
import dev.vasyl.proj.dto.CreateBookRequestDto;
import dev.vasyl.proj.model.Book;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    Book toModel(CreateBookRequestDto createBookRequestDto);

    default Book toModel(CreateBookRequestDto createBookRequestDto, Long id) {
        Book book = toModel(createBookRequestDto);
        book.setId(id);
        return book;
    }

    BookDto toDto(Book book);

    List<BookDto> toBookDtoList(List<Book> books);
}
