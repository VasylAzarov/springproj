package com.example.mate.repository;

import com.example.mate.model.Book;
import java.util.List;

public interface BookRepository {

    Book save(Book book);

    List<Book> findAll();
}
