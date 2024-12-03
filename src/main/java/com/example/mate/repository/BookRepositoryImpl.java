package com.example.mate.repository;

import com.example.mate.model.Book;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Book save(Book book) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(book);
        session.getTransaction().commit();
        return book;
    }

    @Override
    public List<Book> findAll() {
        Session session = sessionFactory.getCurrentSession();
        HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
        JpaCriteriaQuery<Book> query = builder.createQuery(Book.class);
        query.distinct(true);
        query.from(Book.class);
        return session.createQuery(query).getResultList();
    }
}
