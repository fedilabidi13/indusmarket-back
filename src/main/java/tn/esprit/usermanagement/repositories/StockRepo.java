package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.usermanagement.entities.Stock;

public interface StockRepo extends JpaRepository< Stock,Integer> {
}
