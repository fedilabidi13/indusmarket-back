package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.usermanagement.entities.ConfirmationToken;
import tn.esprit.usermanagement.entities.PhoneToken;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PhoneTokenRepo extends JpaRepository<PhoneToken,Integer> {
    Optional<PhoneToken> findByToken(String token);
    @Transactional
    @Modifying
    @Query("UPDATE PhoneToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);
}
