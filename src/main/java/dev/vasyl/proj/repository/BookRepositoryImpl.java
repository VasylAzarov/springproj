package dev.vasyl.proj.repository;

import dev.vasyl.proj.model.Book;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Override
    public Book save(Book book) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
            return book;
        } catch (RuntimeException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to save entity: " + book, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public List<Book> findAll() {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("SELECT b FROM Book b", Book.class).list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
