package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.usermanagement.entities.Message;

public interface MessageRepository extends JpaRepository<Message, Integer>{

}
