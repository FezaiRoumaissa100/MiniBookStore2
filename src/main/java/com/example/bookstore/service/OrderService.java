package com.example.bookstore.service;

import com.example.bookstore.models.Book;
import com.example.bookstore.models.Order;
import com.example.bookstore.models.OrderItem;
import com.example.bookstore.models.OrderStatus;
import com.example.bookstore.models.User;
import com.example.bookstore.repositories.BookRepository;
import com.example.bookstore.repositories.OrderRepository;
import com.example.bookstore.repositories.UserRepository;
import com.example.bookstore.exception.OrderProcessingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
            BookRepository bookRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.cartService = cartService;
    }

    @Transactional
    public Order placeOrder(String username, List<OrderItem> items) {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new OrderProcessingException("User not found"));

            if (items == null || items.isEmpty()) {
                throw new OrderProcessingException("Order must contain at least one item");
            }

            Order order = new Order();
            order.setUser(user);
            order.setOrderDate(LocalDateTime.now());


            double totalAmount = 0.0;
            for (OrderItem item : items) {
                Book book = bookRepository.findById(item.getBook().getId())
                        .orElseThrow(() -> new OrderProcessingException("Book not found: " + item.getBook().getId()));

                if (book.getStock() < item.getQuantity()) {
                    throw new OrderProcessingException("Insufficient stock for book: " + book.getTitle());
                }

                item.setOrder(order);
                item.setPriceAtPurchase(book.getPrice());
                totalAmount += book.getPrice() * item.getQuantity();

                book.setStock(book.getStock() - item.getQuantity());
                bookRepository.save(book);
            }

            order.setTotalAmount(totalAmount);
            order.setItems(items);

            Order savedOrder = orderRepository.save(order);
            cartService.clearCart(username);
            return savedOrder;
        } catch (Exception e) {
            throw new OrderProcessingException("Error processing order: " + e.getMessage(), e);
        }
    }

    public List<Order> getOrdersForUser(String username) {
        return orderRepository.findByUserUsername(username);
    }

    public Optional<Order> getOrderByIdAndUsername(Long orderId, String username) {
        return orderRepository.findByIdAndUserUsername(orderId, username);
    }
}