package dev.vasyl.proj.mapper;

import dev.vasyl.proj.config.MapperConfig;
import dev.vasyl.proj.dto.BookDto;
import dev.vasyl.proj.dto.CreateBookRequestDto;
import dev.vasyl.proj.model.Book;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto createBookRequestDto);

    List<BookDto> toBookDtoList(List<Book> books);
}
