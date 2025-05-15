package com.example.bookstore.service;

import com.example.bookstore.exception.CartProcessingException;
import com.example.bookstore.models.Book;
import com.example.bookstore.models.CartItem;
import com.example.bookstore.models.User;
import com.example.bookstore.repositories.BookRepository;
import com.example.bookstore.repositories.CartItemRepository;
import com.example.bookstore.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public CartServiceImpl(CartItemRepository cartItemRepository,
            BookRepository bookRepository,
            UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void addToCart(Long bookId, int quantity, String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new CartProcessingException("User not found"));
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new CartProcessingException("Book not found"));

            if (quantity <= 0) {
                throw new CartProcessingException("Quantity must be greater than 0");
            }

            if (book.getStock() < quantity) {
                throw new CartProcessingException("Insufficient stock available");
            }

            CartItem cartItem = cartItemRepository.findByUserAndBook(user, book)
                    .orElseGet(() -> {
                        CartItem newItem = new CartItem();
                        newItem.setUser(user);
                        newItem.setBook(book);
                        newItem.setQuantity(0);
                        return newItem;
                    });

            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } catch (Exception e) {
            throw new CartProcessingException("Failed to add item to cart: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void removeFromCart(Long bookId, String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new CartProcessingException("User not found"));
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new CartProcessingException("Book not found"));

            cartItemRepository.deleteByUserAndBook(user, book);
        } catch (Exception e) {
            throw new CartProcessingException("Failed to remove item from cart: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void updateCartItemQuantity(Long bookId, int quantity, String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new CartProcessingException("User not found"));
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new CartProcessingException("Book not found"));

            if (quantity <= 0) {
                throw new CartProcessingException("Quantity must be greater than 0");
            }

            if (book.getStock() < quantity) {
                throw new CartProcessingException("Insufficient stock available");
            }

            CartItem cartItem = cartItemRepository.findByUserAndBook(user, book)
                    .orElseThrow(() -> new CartProcessingException("Cart item not found"));

            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        } catch (Exception e) {
            throw new CartProcessingException("Failed to update cart item quantity: " + e.getMessage(), e);
        }
    }

    @Override
    public List<CartItem> getCartItems(String username) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new CartProcessingException("User not found"));
            return cartItemRepository.findByUser(user);
        } catch (Exception e) {
            throw new CartProcessingException("Failed to get cart items: " + e.getMessage(), e);
        }
    }

    @Override
    public double getCartTotal(String username) {
        try {
            List<CartItem> cartItems = getCartItems(username);
            return cartItems.stream()
                    .mapToDouble(item -> item.getBook().getPrice() * item.getQuantity())
                    .sum();
        } catch (Exception e) {
            throw new CartProcessingException("Failed to calculate cart total: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void clearCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CartProcessingException("User not found"));
        cartItemRepository.deleteAll(cartItemRepository.findByUser(user));
    }
}
