package com.example.bookstore.service;

import com.example.bookstore.models.Book;
import com.example.bookstore.models.CartItem;
import com.example.bookstore.repositories.BookRepository;
import com.example.bookstore.controllers.ShoppingCartController;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CartServiceImpl implements CartService {
    
    private final Map<String, Map<Long, CartItem>> userCarts = new ConcurrentHashMap<>();
    private final BookRepository bookRepository;

    public CartServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void addToCart(Long bookId, int quantity, String username) {
        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new ShoppingCartController.CartProcessingException("Book not found: " + bookId));

            if (book.getStock() < quantity) {
                throw new ShoppingCartController.CartProcessingException("Insufficient stock for book: " + book.getTitle());
            }

            Map<Long, CartItem> userCart = userCarts.computeIfAbsent(username, k -> new HashMap<>());
            
            CartItem existingItem = userCart.get(bookId);
            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
            } else {
                CartItem newItem = new CartItem();
                newItem.setBook(book);
                newItem.setQuantity(quantity);
                userCart.put(bookId, newItem);
            }
        } catch (Exception e) {
            throw new ShoppingCartController.CartProcessingException("Failed to add item to cart", e);
        }
    }

    @Override
    public void removeFromCart(Long bookId, String username) {
        Map<Long, CartItem> userCart = userCarts.get(username);
        if (userCart != null) {
            userCart.remove(bookId);
        }
    }

    @Override
    public void updateCartItemQuantity(Long bookId, int quantity, String username) {
        try {
            Map<Long, CartItem> userCart = userCarts.get(username);
            if (userCart == null || !userCart.containsKey(bookId)) {
                throw new ShoppingCartController.CartProcessingException("Item not found in cart");
            }

            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new ShoppingCartController.CartProcessingException("Book not found: " + bookId));

            if (book.getStock() < quantity) {
                throw new ShoppingCartController.CartProcessingException("Insufficient stock for book: " + book.getTitle());
            }

            CartItem item = userCart.get(bookId);
            item.setQuantity(quantity);
        } catch (Exception e) {
            throw new ShoppingCartController.CartProcessingException("Failed to update cart item quantity", e);
        }
    }

    @Override
    public List<CartItem> getCartItems(String username) {
        Map<Long, CartItem> userCart = userCarts.get(username);
        if (userCart == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(userCart.values());
    }

    @Override
    public double getCartTotal(String username) {
        return getCartItems(username).stream()
                .mapToDouble(item -> item.getBook().getPrice() * item.getQuantity())
                .sum();
    }

    @Override
    public void clearCart(String username) {
        userCarts.remove(username);
    }
}
