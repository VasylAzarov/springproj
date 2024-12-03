package com.example.mate.service;

import com.example.mate.model.Book;
import com.example.mate.repository.BookRepository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book add(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book getById(long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book update(Book book) {
        if(bookRepository.findById(book.getId()).isPresent()){
            return bookRepository.save(book);
        }
        return null;
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }
}
