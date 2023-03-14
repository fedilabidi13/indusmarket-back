package tn.esprit.usermanagement.servicesImpl.ChatServiceImpl;

import com.cloudinary.Cloudinary;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.ChatEntities.Message;
import tn.esprit.usermanagement.entities.ForumEntities.Media;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.MediaRepo;
import tn.esprit.usermanagement.repositories.MessageRepository;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.MessageIservice;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;
import tn.esprit.usermanagement.servicesImpl.ForumServiceImpl.DataLoadServiceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageIservice {
    MessageRepository messageRepository;
    UserRepo userRepo;
    private AuthenticationService authenticationService;
    private DataLoadServiceImpl dataLoadService;
    Cloudinary cloudinary;
    MediaRepo mediaRepo;



    public ResponseEntity<?> sendMessage(String body, String reciverEmail, List<MultipartFile> files) throws IOException {
        User sender = authenticationService.currentlyAuthenticatedUser();
        User receiver = userRepo.findByEmail(reciverEmail).get();
        if (receiver == null) {
            return ResponseEntity.badRequest().body("Receiver not found");
        }
        Message message = new Message();
        if (files==null||files.isEmpty()) {
            message.setMedias(null);
            message.setBody(body);
            message.setDate(LocalDateTime.now());
            message.setSender(sender);
            message.setReciver(receiver);
            messageRepository.save(message);
            return ResponseEntity.ok().body(message);
        }
        else{
        List<Media> mediaList = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            Media media = new Media();
            String url = cloudinary.uploader()
                    .upload(multipartFile.getBytes(),
                            Map.of("public_id", UUID.randomUUID().toString()))
                    .get("url")
                    .toString();
            media.setImagenUrl(url);
            media.setName(multipartFile.getName());
            mediaList.add(media);
        }
        mediaRepo.saveAll(mediaList);
        message.setMedias(mediaList);
        message.setBody(body);
        message.setDate(LocalDateTime.now());
        message.setSender(sender);
        message.setReciver(receiver);
        messageRepository.save(message);
        return ResponseEntity.ok().body(message);
        }
    }

    public List<Message> getMessagesBySenderAndReceiver(User sender, User reciver) {
        return messageRepository.findBySenderAndReciverOrderByDateAsc(sender, reciver);
    }
    public List<Message> Searchmessage(String ch, Integer id) {
        User sender = authenticationService.currentlyAuthenticatedUser();
        User receiver = userRepo.findById(id).orElse(null);
        if (receiver == null) {
            return Collections.emptyList();
        }
        List<Message> matchingMessages = messageRepository.findBySenderAndReciverOrderByDateAsc(sender, receiver).stream()
                .filter(message -> message.getBody().contains(ch))
                .collect(Collectors.toList());
        dataLoadService.DetctaDataLoad(ch,id);
        return matchingMessages;
    }
}
