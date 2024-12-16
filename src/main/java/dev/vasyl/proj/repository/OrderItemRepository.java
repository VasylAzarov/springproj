package dev.vasyl.proj.repository;

import dev.vasyl.proj.model.OrderItem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @EntityGraph(attributePaths = "book")
    Page<OrderItem> findAllByOrderId(Long orderId, Pageable pageable);

    @EntityGraph(attributePaths = "book")
    Optional<OrderItem> findById(Long id);

}
