package tn.esprit.usermanagement.repositories;

import tn.esprit.usermanagement.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Orders,Integer> {
}
