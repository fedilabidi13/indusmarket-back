package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.Query;
import tn.esprit.usermanagement.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepo extends JpaRepository<CartItem,Integer> {
    @Query("select c from CartItem c where c.product.reference = ?1")
    CartItem findAllByProductReference(String refProduct);
}
