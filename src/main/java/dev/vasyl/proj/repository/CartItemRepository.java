package dev.vasyl.proj.repository;

import dev.vasyl.proj.model.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findById(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE CartItem c SET c.isDeleted = true WHERE "
            + "c.shoppingCart.id = :shoppingCartId AND c.isDeleted = false")
    void deleteAllByShoppingCartId(Long shoppingCartId);

}
