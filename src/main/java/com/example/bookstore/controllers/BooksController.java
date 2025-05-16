package com.example.bookstore.controllers;

import com.example.bookstore.models.Book;
import com.example.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BooksController {

    @Autowired
    private BookService bookService;

    @GetMapping({"", "/", "/list"})
    public String listBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            Model model) {
        
        Page<Book> books;
        if (search != null && !search.isEmpty()) {
            books = bookService.searchBooks(search, PageRequest.of(page, size));
        } else {
            books = bookService.getAllBooks(PageRequest.of(page, size));
        }

        model.addAttribute("books", books);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", books.getTotalPages());
        model.addAttribute("search", search);
        return "books/list";
    }

    @GetMapping("/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        return "books/view";
    }

    public static class BookNotFoundException extends RuntimeException {
        public BookNotFoundException(String message) {
            super(message);
        }
        public BookNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }
    }
} 