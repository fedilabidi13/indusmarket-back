package tn.esprit.usermanagement.repositories;

import tn.esprit.usermanagement.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepo extends JpaRepository<CartItem,Integer> {
}
