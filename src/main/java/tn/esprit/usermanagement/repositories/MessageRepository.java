package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.messaging.Message;

public interface MessageRepository extends JpaRepository<Message, Integer>{

}
