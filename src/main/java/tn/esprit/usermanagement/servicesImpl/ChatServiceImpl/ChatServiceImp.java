package tn.esprit.usermanagement.servicesImpl.ChatServiceImpl;

import com.cloudinary.Cloudinary;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.ChatEntities.Chatroom;
import tn.esprit.usermanagement.entities.ChatEntities.Message;
import tn.esprit.usermanagement.entities.ForumEntities.Media;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.ChatroomRepo;

import tn.esprit.usermanagement.repositories.MediaRepo;
import tn.esprit.usermanagement.repositories.MessageRepository;
import tn.esprit.usermanagement.services.ChatIservice;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class ChatServiceImp implements ChatIservice {

    ChatroomRepo chatroomRepo;
    AuthenticationService authenticationService;
    MessageRepository messageRepo;
    Cloudinary cloudinary;
    MediaRepo mediaRepo;

    @Override
    public Chatroom createChatroom( Chatroom chatroom)  {
        chatroom.setCreatedAt(LocalDateTime.now());
        chatroom.setCodeRoom(generateCodeRoom());
        return chatroomRepo.save(chatroom);

    }
    @Override
    public String generateCodeRoom() {
        String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int length = 10;
        StringBuilder builder = new StringBuilder();
        builder.append("chat");
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            builder.append(allowedCharacters.charAt(random.nextInt(allowedCharacters.length())));
        }
        return builder.toString();
    }

    public Chatroom findByCodeRoom(String codeRoom) {
        return chatroomRepo.findByCodeRoom(codeRoom);
    }

    public Chatroom addUserToChatroom(Chatroom chatroom) {
        User user = authenticationService.currentlyAuthenticatedUser();
        chatroom.getRecievres().add(user);
        return chatroomRepo.save(chatroom);
    }
    public ResponseEntity<?> sendMessageToChatroom(String messageBody, Integer chatroomId, List<MultipartFile> files) throws IOException {
        // Get currently authenticated user
        User sender = authenticationService.currentlyAuthenticatedUser();

        // Find chatroom by ID
        Chatroom chatroom = chatroomRepo.findById(chatroomId)
                .orElseThrow(() -> new RuntimeException("Chatroom not found."));

        // Check if sender is a member of the chatroom
        if (!chatroom.getRecievres().contains(sender)) {
            return ResponseEntity.badRequest().body("User is not a member of the chatroom.");
        }

        // Create message object
        Message message = new Message();
        message.setBody(messageBody);
        message.setChatroom(chatroom);
        message.setSender(sender);
        message.setDate(LocalDateTime.now());

        // Add media files to message if any
        if (files != null && !files.isEmpty()) {
            List<Media> mediaList = new ArrayList<>();
            for (MultipartFile multipartFile : files) {
                Media media = new Media();
                String url = cloudinary.uploader().upload(multipartFile.getBytes(),
                        Map.of("public_id", UUID.randomUUID().toString())).get("url").toString();
                media.setImagenUrl(url);
                media.setName(multipartFile.getName());
                mediaList.add(media);
            }
            mediaRepo.saveAll(mediaList);
            message.setMedias(mediaList);
        }

        // Save message to database
        messageRepo.save(message);
        return ResponseEntity.ok().body(message);
    }



    public List<Message> getAllMessagesByChatroomIdSortedByDate(Integer chatroomId) {
        return messageRepo.findAllByChatroomIdOrderByDate(chatroomId);
    }
    public Message findMessageByIdAndChatroomId(Integer messageId, Integer chatroomId) {
        Chatroom chatroom = chatroomRepo.findById(chatroomId)
                .orElseThrow(() -> new RuntimeException("Chatroom not found."));
        return messageRepo.findByMessageIdAndChatroomId(messageId, chatroom.getId());
    }

}
