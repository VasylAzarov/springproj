package dev.vasyl.proj;

import dev.vasyl.proj.model.Book;
import dev.vasyl.proj.service.BookService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class Application implements CommandLineRunner {
    @Autowired
    private final BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        Book book = Book.builder()
                .title("title1")
                .author("author1")
                .isbn("isbn1")
                .price(BigDecimal.valueOf(11.1))
                .description("description1")
                .coverImage("coverImg1")
                .build();
        book = bookService.save(book);
        System.out.println(book);
        Book book2 = Book.builder()
                .title("title2")
                .author("author2")
                .isbn("isbn2")
                .price(BigDecimal.valueOf(22.2))
                .description("description2")
                .coverImage("coverImg2")
                .build();
        book2 = bookService.save(book2);
        System.out.println(book2);
        System.out.println(bookService.findAll());
    }
}
