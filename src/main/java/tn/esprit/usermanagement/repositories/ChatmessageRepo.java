package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.usermanagement.entities.ChatMessage;

public interface ChatmessageRepo extends JpaRepository<ChatMessage, Integer>{

}
