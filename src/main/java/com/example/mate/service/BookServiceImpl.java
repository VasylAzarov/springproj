package com.example.mate.service;

import com.example.mate.model.Book;
import com.example.mate.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book add(Book book) {
        return null;
    }

    @Override
    public Book getById(long id) {
        return null;
    }

    @Override
    public List<Book> getAll() {
        return new ArrayList<>();
    }

    @Override
    public Book update(Book book) {
        return null;
    }

    @Override
    public void delete(Book book) {

    }
}
