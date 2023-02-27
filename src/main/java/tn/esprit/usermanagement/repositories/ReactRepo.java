package tn.esprit.usermanagement.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.React;

@Repository
public interface ReactRepo extends JpaRepository<React, Integer>{

	
	/*@Query(value =" SELECT * from users u  INNER JOIN post_like p ON p.user_user_id = u.user_id ORDER BY count(*)",nativeQuery=true)
			public Set<Object> USer_order_by_Like ();*/
}
