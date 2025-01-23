INSERT INTO categories (name, description, is_deleted) VALUES ('Category 1', 'Category 1', 0);
INSERT INTO categories (name, description, is_deleted) VALUES ('Category 2', 'Category 2', 0);
INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1, 'Book 1', 'Author 1', '123-12-1234-123-1', 11.0, 'Description 1', 'images.com/image1.jpg', 0);
INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (2, 'Book 2', 'Author 2', '123-12-1234-123-2', 22.0, 'Description 2', 'images.com/image2.jpg', 0);
INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (3, 'Book 3', 'Author 3', '123-12-1234-123-3', 33.0, 'Description 3', 'images.com/image3.jpg', 0);
INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);
INSERT INTO books_categories (book_id, category_id) VALUES (2, 1);
INSERT INTO books_categories (book_id, category_id) VALUES (3, 2);