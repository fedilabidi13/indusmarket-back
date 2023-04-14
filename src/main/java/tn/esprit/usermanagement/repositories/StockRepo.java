package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.Stock;
@Repository
public interface StockRepo extends JpaRepository< Stock,Integer> {
}
