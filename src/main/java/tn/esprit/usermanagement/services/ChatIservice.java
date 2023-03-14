package tn.esprit.usermanagement.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.ChatEntities.Chatroom;
import tn.esprit.usermanagement.entities.ChatEntities.Message;

import java.io.IOException;
import java.util.List;

public interface ChatIservice {
    String generateCodeRoom() ;
    Chatroom createChatroom(Chatroom chatroom)  ;
    Chatroom findByCodeRoom(String codeRoom) ;
    Chatroom addUserToChatroom(Chatroom chatroom) ;
    public ResponseEntity<?> sendMessageToChatroom(String messageBody, Integer chatroomId, List<MultipartFile> files) throws IOException ;
    List<Message> getAllMessagesByChatroomIdSortedByDate(Integer chatroomId);
    Message findMessageByIdAndChatroomId(Integer messageId, Integer chatroomId);




}
