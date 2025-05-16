-- Users
INSERT INTO users (email, password, username, role) VALUES
    ('admin@bookstore.com', '$2a$12$I5N8IYlOCZZRUoESU8JFuuRX6AGhGX7Jhe2wotULuwBYufZeHMNxO', 'admin', 'ADMIN'),
('user@bookstore.com', '$2a$12$nGkrTB0WOi0pPNSbbY4IWeceMxakaWNzjIXVcpJGFJM8qgF2X0lvu', 'user', 'USER'),
('testuser@example.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'testuser', 'USER');
-- Books
INSERT INTO books (title, author, price, stock, description, image) VALUES
('Harry Potter', 'J.K Rowling ', 150, 10, 'The famous Harry Potter', 'Harry_Potter.jpg'),
('Spring in Action', 'Craig Walls', 42.75, 12, 'Guide to Spring Framework', 'spring_in_action.jpg'),
('To Kill a Mockingbird', 'Harper Lee', 29.99, 15, 'A novel about the serious issues of rape and racial inequality.', 'to_kill_a_mockingbird.jpg'),
('1984', 'George Orwell', 24.99, 20, 'A dystopian social science fiction novel and cautionary tale about the future.', '1948.jpg'),
('The Great Gatsby', 'F. Scott Fitzgerald', 22.50, 10, 'A story of the mysteriously wealthy Jay Gatsby and his love for Daisy Buchanan.', 'The_Great_Gatsby.jpg'),
('Moby Dick', 'Herman Melville', 27.00, 8, 'The narrative of Captain Ahab s obsessive quest to kill the white whale.', 'moby_dick.jpg');
