package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.CategoryAdve;

@Repository
public interface CategoryAdverRepo extends JpaRepository<CategoryAdve, String>{
	
}
