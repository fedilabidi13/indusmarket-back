package tn.esprit.usermanagement.servicesImpl.ChatServiceImpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.ChatEntities.Chatroom;
import tn.esprit.usermanagement.entities.ChatEntities.Message;
import tn.esprit.usermanagement.entities.ForumEntities.Post;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.ChatroomRepo;

import tn.esprit.usermanagement.repositories.MessageRepository;
import tn.esprit.usermanagement.services.ChatIservice;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;
import tn.esprit.usermanagement.servicesImpl.ForumServiceImpl.DataLoadServiceImpl;
import tn.esprit.usermanagement.servicesImpl.ForumServiceImpl.ImageServiceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class ChatServiceImp implements ChatIservice {

    ChatroomRepo chatroomRepo;
    AuthenticationService authenticationService;
    MessageRepository messageRepo;
    ImageServiceImpl imageService;
    private DataLoadServiceImpl dataLoadService;

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
    public void sendMessageToChatroom(String message, Integer chatroomId, List<MultipartFile> files) throws IOException {
        Chatroom chatroom = chatroomRepo.findById(chatroomId).orElseThrow(() -> new RuntimeException("Chatroom not found."));
        User sender = authenticationService.currentlyAuthenticatedUser();
        Message chatroomMessage = new Message();
        chatroomMessage.setPictures(imageService.addimages(files));
        chatroomMessage.setBody(message);
        chatroomMessage.setChatroom(chatroom);
        chatroomMessage.setDate(LocalDateTime.now());
        chatroomMessage.setSender(sender);
        List<User> chatroomUsers = chatroom.getRecievres();
        for (User user : chatroomUsers) {
            Message userMessage = new Message();
            userMessage.setPictures(imageService.addimages(files));
            userMessage.setBody(message);
            userMessage.setChatroom(chatroom);
            userMessage.setDate(LocalDateTime.now());
            userMessage.setSender(sender);
            userMessage.setReciver(user);
            messageRepo.save(userMessage);
        }
        messageRepo.save(chatroomMessage);
    }
    public List<Message> getAllMessagesByChatroomIdSortedByDate(Integer chatroomId) {
        return messageRepo.findAllByChatroomIdOrderByDate(chatroomId);
    }
    public Message findMessageByIdAndChatroomId(Integer messageId, Integer chatroomId) {
        Chatroom chatroom = chatroomRepo.findById(chatroomId)
                .orElseThrow(() -> new RuntimeException("Chatroom not found."));
        return messageRepo.findByMessageIdAndChatroomId(messageId, chatroom.getId());
    }
    public void deleteMessageByIdAndSender(Integer messageId) {
        User user = authenticationService.currentlyAuthenticatedUser();
        messageRepo.deleteByMessageIdAndSenderId(messageId, user.getId());
    }

}
