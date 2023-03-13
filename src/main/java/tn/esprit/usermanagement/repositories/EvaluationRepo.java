package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.Evaluation;

import java.util.List;

@Repository
public interface EvaluationRepo extends JpaRepository<Evaluation,Integer> {
   @Query("select e from Evaluation e where e.deliveryId.id = : deliveryId")
   public List<Evaluation> findByDeliveryId(Integer deliveryId);
}
