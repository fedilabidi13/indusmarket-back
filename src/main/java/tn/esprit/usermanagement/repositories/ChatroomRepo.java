package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.Chatroom;

@Repository
public interface ChatroomRepo extends JpaRepository<Chatroom, Integer>{

}
