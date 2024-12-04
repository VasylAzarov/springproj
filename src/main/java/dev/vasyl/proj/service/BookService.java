package dev.vasyl.proj.service;

import dev.vasyl.proj.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
