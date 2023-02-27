package tn.esprit.usermanagement.repositories;

import tn.esprit.usermanagement.entities.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepo extends JpaRepository<ShoppingCart,Integer> {
}
