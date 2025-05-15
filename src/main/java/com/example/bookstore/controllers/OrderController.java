package com.example.bookstore.controllers;

import com.example.bookstore.models.Order;
import com.example.bookstore.service.OrderService;
import com.example.bookstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @GetMapping("/checkout")
    public String showCheckout(Model model, Authentication authentication) {
        String username = authentication.getName();
        model.addAttribute("cartItems", cartService.getCartItems(username));
        model.addAttribute("total", cartService.getCartTotal(username));
        return "orders/checkout";
    }

    @PostMapping("/place")
    public String placeOrder(Authentication authentication) {
        String username = authentication.getName();
        List<com.example.bookstore.models.CartItem> cartItems = cartService.getCartItems(username);
        if (cartItems.isEmpty()) {
            return "redirect:/cart";
        }
        java.util.List<com.example.bookstore.models.OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            com.example.bookstore.models.OrderItem orderItem = new com.example.bookstore.models.OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getBook().getPrice());
            return orderItem;
        }).collect(java.util.stream.Collectors.toList());
        Order savedOrder = orderService.placeOrder(username, orderItems);
        return "redirect:/orders/confirmation/" + savedOrder.getId();
    }

    @GetMapping("/confirmation/{orderId}")
    public String showConfirmation(@PathVariable Long orderId, Model model, Authentication authentication) {
        Order order = orderService.getOrderByIdAndUsername(orderId, authentication.getName())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        model.addAttribute("order", order);
        return "orders/confirmation";
    }

    @GetMapping("/history")
    public String showOrderHistory(Model model, Authentication authentication) {
        model.addAttribute("orders", orderService.getOrdersForUser(authentication.getName()));
        return "orders/history";
    }
}