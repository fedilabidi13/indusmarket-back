package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.usermanagement.entities.Orders;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.entities.ProductRequest;

import java.util.List;

public interface ProductRequestRepo extends JpaRepository<ProductRequest,Integer> {


    List<ProductRequest> findByProduct(Product product);
}
