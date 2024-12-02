package com.example.mate.service;

import com.example.mate.model.Book;

import java.util.List;

public interface BookService {

    Book add(Book book);

    Book getById(long id);

    List<Book> getAll();

    Book update(Book book);

    void delete(Book book);

}
