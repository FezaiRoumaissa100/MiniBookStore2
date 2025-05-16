package com.example.bookstore.config;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.security.access.AccessDeniedException;

import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.exception.OrderNotFoundException;
import com.example.bookstore.exception.OrderProcessingException;
import com.example.bookstore.exception.CartProcessingException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class GlobalExceptionHandler {

    private String formatTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleBookNotFound(BookNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorType", "Book Not Found");
        model.addAttribute("timestamp", formatTimestamp());
        return "error";
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleOrderNotFound(OrderNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorType", "Order Not Found");
        model.addAttribute("timestamp", formatTimestamp());
        return "error";
    }

    @ExceptionHandler(OrderProcessingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleOrderProcessing(OrderProcessingException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorType", "Order Processing Error");
        model.addAttribute("timestamp", formatTimestamp());
        return "error";
    }

    @ExceptionHandler(CartProcessingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleCartProcessing(CartProcessingException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorType", "Cart Processing Error");
        model.addAttribute("timestamp", formatTimestamp());
        return "error";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(AccessDeniedException ex, Model model) {
        model.addAttribute("errorMessage", "You do not have permission to access this resource");
        model.addAttribute("errorType", "Access Denied");
        model.addAttribute("timestamp", formatTimestamp());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
        model.addAttribute("errorType", "Internal Server Error");
        model.addAttribute("timestamp", formatTimestamp());
        return "error";
    }
} 