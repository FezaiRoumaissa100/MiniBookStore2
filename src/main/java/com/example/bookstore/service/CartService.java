package com.example.bookstore.service;

import com.example.bookstore.models.CartItem;
import java.util.List;

public interface CartService {
    void addToCart(Long bookId, int quantity, String username);

    void removeFromCart(Long bookId, String username);

    void updateCartItemQuantity(Long bookId, int quantity, String username);

    List<CartItem> getCartItems(String username);

    double getCartTotal(String username);

    void clearCart(String username);
}
