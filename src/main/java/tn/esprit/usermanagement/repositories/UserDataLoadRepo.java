package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.UserDataLoad;

@Repository
public interface UserDataLoadRepo extends JpaRepository<UserDataLoad,Integer>{

}
