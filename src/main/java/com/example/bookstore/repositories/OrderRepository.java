package com.example.bookstore.repositories;
import com.example.bookstore.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.id = :orderId AND o.user.username = :username")
    Optional<Order> findByIdAndUserUsername(@Param("orderId") Long orderId, @Param("username") String username);

    List<Order> findByUserUsername(String username);
}
