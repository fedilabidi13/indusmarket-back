package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.usermanagement.entities.*;
import tn.esprit.usermanagement.enumerations.*;

import java.time.LocalDateTime;
import java.util.List;

public interface ClaimsRepo extends JpaRepository<Claims,Integer> {

    List<Claims> findByTypeClaim(TypeClaim typeClaim);
    List<Claims> findByUser(int UserId);
    @Query("select c from Claims c where c.statusClaims=?1 AND  c.CreatedAt<?2 ")
    List<Claims> findByStatusClaimsAndCreatedAtIsBefore(StatusClaims statusClaims, LocalDateTime limite);
    @Query("select c from Claims c where c.statusClaims=?1 AND  c.ConsultAt<?2 ")
    List<Claims> findByStatusClaimsAndConsultAtIsBefore(StatusClaims statusClaims, LocalDateTime limite);
}
