package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.ChatEntities.Chatroom;

import java.util.List;

@Repository
public interface ChatroomRepo extends JpaRepository<Chatroom, Integer>{
    Chatroom findByCodeRoom(String codeRoom);

}
