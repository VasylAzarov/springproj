package dev.vasyl.proj.repository;

import dev.vasyl.proj.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
