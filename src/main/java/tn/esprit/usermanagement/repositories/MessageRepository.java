package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.usermanagement.entities.ChatEntities.Message;
import tn.esprit.usermanagement.entities.User;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer>{
    List<Message> findBySenderAndReciverOrderByDateAsc(User sender, User reciver);
    List<Message> findAllByChatroomIdOrderByDate(Integer chatroomId);
    Message findByMessageIdAndChatroomId(Integer messageId, Integer chatroomId);
    void deleteByMessageIdAndSenderId(Integer messageId, Integer userId);



}
