package tn.esprit.usermanagement.servicesImpl.ChatServiceImpl;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.ChatEntities.Message;
import tn.esprit.usermanagement.entities.ForumEntities.Post;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.MessageRepository;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.MessageIservice;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;
import tn.esprit.usermanagement.servicesImpl.ForumServiceImpl.DataLoadServiceImpl;
import tn.esprit.usermanagement.servicesImpl.ForumServiceImpl.ImageServiceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageIservice {
    MessageRepository messageRepository;
    UserRepo userRepo;
    private AuthenticationService authenticationService;
    private DataLoadServiceImpl dataLoadService;
    ImageServiceImpl imageService;


    public ResponseEntity<?> sendMessage(String body, String reciverEmail, List<MultipartFile> files) throws IOException {
        User sender = authenticationService.currentlyAuthenticatedUser();
        User receiver = userRepo.findByEmail(reciverEmail).get();
        if (receiver == null) {
            return ResponseEntity.badRequest().body("Receiver not found");
        }
        Message message = new Message();
        message.setPictures(imageService.addimages(files));
        message.setBody(body);
        message.setDate(LocalDateTime.now());
        message.setSender(sender);
        message.setReciver(receiver);
        messageRepository.save(message);
        return ResponseEntity.ok().body(message);
    }

    public List<Message> getMessagesBySenderAndReceiver(User sender, User reciver) {
        return messageRepository.findBySenderAndReciverOrderByDateAsc(sender, reciver);
    }
    public List<Message> Searchmessage(String ch, Integer id){
        List<Message> ll = new ArrayList<>();
        for (Message message : messageRepository.findAll()) {
            if (message.getBody().contains(ch) )
                ll.add(message);
        }
        dataLoadService.DetctaDataLoad(ch,id);
        return ll;
    }

}
