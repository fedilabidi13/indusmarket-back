package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.Chatroom;

import java.util.List;

@Repository
public interface ChatroomRepo extends JpaRepository<Chatroom, Integer>{
    List<Chatroom> findBySender_IdOrReciver_Id(Integer senderId, Integer receiverId);

}
