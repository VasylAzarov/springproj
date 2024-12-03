package com.example.mate.service;

import com.example.mate.model.Book;
import java.util.List;

public interface BookService {

    Book save(Book book);

    List<Book> findAll();
}
