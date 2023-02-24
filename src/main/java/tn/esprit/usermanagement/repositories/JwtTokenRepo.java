package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.usermanagement.entities.JwtToken;

import java.util.List;
import java.util.Optional;

public interface JwtTokenRepo extends JpaRepository<JwtToken,Integer> {
    @Query (value = """
      select t from JwtToken t inner join User u\s
      on t.user.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s """)
    List<JwtToken> findAllValidTokenByUser(Integer id);

    Optional<JwtToken> findByToken(String token);
}
