package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.usermanagement.entities.ChatEntities.Message;

public interface MessageRepository extends JpaRepository<Message, Integer>{

}
