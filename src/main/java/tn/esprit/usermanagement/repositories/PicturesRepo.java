package tn.esprit.usermanagement.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.ForumEntities.Pictures;

@Repository

public interface PicturesRepo extends JpaRepository<Pictures,Integer> {
}
