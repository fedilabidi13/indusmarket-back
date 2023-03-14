package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.usermanagement.entities.Orders;
import tn.esprit.usermanagement.entities.ProductRequest;

public interface ProductRequestRepo extends JpaRepository<ProductRequest,Integer> {



}
