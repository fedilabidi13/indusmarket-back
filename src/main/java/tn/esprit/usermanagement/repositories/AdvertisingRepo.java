package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.Advertising;

@Repository
public interface AdvertisingRepo extends JpaRepository<Advertising, Integer>{

}
