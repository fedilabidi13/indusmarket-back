package tn.esprit.usermanagement.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.usermanagement.entities.Address;

public interface AddressRepo extends JpaRepository<Address,Integer> {
}

