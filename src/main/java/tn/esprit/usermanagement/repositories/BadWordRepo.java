package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.ForumEntities.BadWord;

@Repository
public interface BadWordRepo extends JpaRepository<BadWord, Integer>{

}
