package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.usermanagement.entities.ChatEntities.ChatMessage;

public interface ChatmessageRepo extends JpaRepository<ChatMessage, Integer>{
    //List<ChatMessage> findByChat_IdOrderByCreatedAtAsc(Integer chatId);

}
