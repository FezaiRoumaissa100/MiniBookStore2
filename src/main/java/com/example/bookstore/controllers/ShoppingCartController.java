package com.example.bookstore.controllers;

import com.example.bookstore.models.CartItem;
import com.example.bookstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public String viewCart(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }
        
        try {
            String username = authentication.getName();
            List<CartItem> cartItems = cartService.getCartItems(username);
            double total = cartService.getCartTotal(username);
            
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("total", total);
            return "cart/view";
        } catch (CartProcessingException e) {
            model.addAttribute("error", e.getMessage());
            return "cart/view";
        }
    }

    @PostMapping("/add/{bookId}")
    public String addToCart(@PathVariable Long bookId, 
                          @RequestParam(defaultValue = "1") int quantity,
                          Authentication authentication,
                          RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }

        try {
            String username = authentication.getName();
            cartService.addToCart(bookId, quantity, username);
            redirectAttributes.addFlashAttribute("success", "Item added to cart successfully");
        } catch (CartProcessingException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/books";
    }

    @PostMapping("/remove/{bookId}")
    public String removeFromCart(@PathVariable Long bookId,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }

        try {
            String username = authentication.getName();
            cartService.removeFromCart(bookId, username);
            redirectAttributes.addFlashAttribute("success", "Item removed from cart successfully");
        } catch (CartProcessingException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/shopping-cart";
    }

    @PostMapping("/update/{bookId}")
    public String updateQuantity(@PathVariable Long bookId,
                               @RequestParam int quantity,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }

        try {
            String username = authentication.getName();
            cartService.updateCartItemQuantity(bookId, quantity, username);
            redirectAttributes.addFlashAttribute("success", "Cart updated successfully");
        } catch (CartProcessingException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/shopping-cart";
    }

    public static class CartProcessingException extends RuntimeException {
        public CartProcessingException(String message) {
            super(message);
        }
        public CartProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
} 