package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.ForumEntities.Post;

@Repository
public interface PostRepo extends JpaRepository<Post, Integer> {
	/*@Query(value="SELECT DATEDIFF(NOW(),:d  )   ", nativeQuery=true)
	public int diffrence_entre_date(@Param("d") Date d);*/
}
