package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.Media;

@Repository
public interface MediaRepo extends JpaRepository<Media, Integer> {
	
}
