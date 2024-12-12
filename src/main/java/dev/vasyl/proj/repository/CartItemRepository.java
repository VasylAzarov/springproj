package dev.vasyl.proj.repository;

import dev.vasyl.proj.model.CartItem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findById(Long id);

    Page<CartItem> findAllByShoppingCartId(Long shoppingCartId, Pageable pageable);
}
