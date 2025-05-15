-- Users
INSERT INTO users (email, password, username, role) VALUES
('admin@bookstore.com', '$2a$12$I5N8IYlOCZZRUoESU8JFuuRX6AGhGX7Jhe2wotULuwBYufZeHMNxO', 'admin', 'ADMIN'),
('user@bookstore.com', '$2a$12$nGkrTB0WOi0pPNSbbY4IWeceMxakaWNzjIXVcpJGFJM8qgF2X0lvu', 'user', 'USER'),
('testuser@example.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'testuser', 'USER');

-- Books
INSERT INTO books (title, author, price, stock, description, image) VALUES
('Clean Code', 'Robert C. Martin', 35.99, 10, 'A handbook of agile software craftsmanship', 'clean_code.jpg'),
('Design Patterns', 'Erich Gamma', 45.50, 5, 'Elements of Reusable Object-Oriented Software', 'design_patterns.jpg'),
('Effective Java', 'Joshua Bloch', 39.99, 8, 'Best practices for Java programming', 'effective_java.jpg'),
('Spring in Action', 'Craig Walls', 42.75, 12, 'Guide to Spring Framework', 'spring_in_action.jpg'),
('The Pragmatic Programmer', 'Andrew Hunt', 37.25, 7, 'Your journey to mastery', 'pragmatic_programmer.jpg');