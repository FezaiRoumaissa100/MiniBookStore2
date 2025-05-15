package com.example.bookstore.service;

import com.example.bookstore.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    Page<Book> getAllBooks(Pageable pageable);
    Page<Book> searchBooks(String searchTerm, Pageable pageable);
    Book getBookById(Long id);
}

