package com.example.mate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String description;
    private String coverImage;

    public Book() {
    }

    public Book(String title, String author, String isbn,
                BigDecimal price, String description, String coverImage) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.description = description;
        this.coverImage = coverImage;
    }

    public Book(long id, String title, String author, String isbn,
                BigDecimal price, String description, String coverImage) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.description = description;
        this.coverImage = coverImage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book book = (Book) o;
        return id == book.id
                && Objects.equals(title, book.title)
                && Objects.equals(author, book.author)
                && Objects.equals(isbn, book.isbn)
                && Objects.equals(price, book.price)
                && Objects.equals(description, book.description)
                && Objects.equals(coverImage, book.coverImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, isbn, price, description, coverImage);
    }

    @Override
    public String toString() {
        return "Book{"
                + "id=" + id
                + ", title='" + title + '\''
                + ", author='" + author + '\''
                + ", isbn='" + isbn + '\''
                + ", price=" + price
                + ", description='" + description + '\''
                + ", coverImage='" + coverImage + '\''
                + '}';
    }
}
