package tn.esprit.usermanagement.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.ChatEntities.Message;
import tn.esprit.usermanagement.entities.User;

import java.io.IOException;
import java.util.List;

public interface MessageIservice {
    public ResponseEntity<?> sendMessage(String body, String reciverEmail, List<MultipartFile> files) throws IOException ;
     List<Message> getMessagesBySenderAndReceiver(User sender, User receiver) ;
    public List<Message> Searchmessage(String ch, Integer id);



    }
