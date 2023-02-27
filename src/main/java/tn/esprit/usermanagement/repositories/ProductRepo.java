package tn.esprit.usermanagement.repositories;

import tn.esprit.usermanagement.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product,Integer> {
}
