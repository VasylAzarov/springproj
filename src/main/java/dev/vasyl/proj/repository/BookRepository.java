package dev.vasyl.proj.repository;

import dev.vasyl.proj.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
