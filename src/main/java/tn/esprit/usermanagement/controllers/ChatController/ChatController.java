package tn.esprit.usermanagement.controllers.ChatController;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.ChatEntities.Chatroom;
import tn.esprit.usermanagement.entities.ChatEntities.Message;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.ChatIservice;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;

import java.io.IOException;
import java.util.List;


@RestController
@AllArgsConstructor
public class ChatController {
ChatIservice chatIservice;
UserRepo userRepo;
AuthenticationService authenticationService;
    @PostMapping("/addChatroom")
    public ResponseEntity<Chatroom> addChatroom(@RequestBody Chatroom chatroom) {
        Chatroom savedChatroom = chatIservice.createChatroom(chatroom);
        return new ResponseEntity<>(savedChatroom, HttpStatus.CREATED);
    }
    @PostMapping("/joinChatroom/{codeRoom}")
    public ResponseEntity<?> joinChatroom(@PathVariable String codeRoom) {
        Chatroom chatroom = chatIservice.findByCodeRoom(codeRoom);
        if (chatroom == null) {
            return ResponseEntity.notFound().build();
        }
        chatroom = chatIservice.addUserToChatroom(chatroom);
        return ResponseEntity.ok(chatroom);
    }
    @PostMapping("sendMessageToChatroom/{chatroomId}")
    public ResponseEntity<String> sendMessageToChatroom(@RequestBody String message, @PathVariable Integer chatroomId, @RequestParam("files") List<MultipartFile> files) throws IOException {
        chatIservice.sendMessageToChatroom(message, chatroomId,files);
        return ResponseEntity.ok("Message sent successfully.");
    }
    @GetMapping("getAllMessagesByChatroomIdSortedByDate/{chatroomId}/messages")
    public ResponseEntity<List<Message>> getAllMessagesByChatroomIdSortedByDate(@PathVariable Integer chatroomId) {
        List<Message> messages = chatIservice.getAllMessagesByChatroomIdSortedByDate(chatroomId);
        return ResponseEntity.ok(messages);
    }
    @GetMapping("/{chatroomId}/messages/{messageId}")
    public ResponseEntity<Message> getMessage(@PathVariable Integer chatroomId, @PathVariable Integer messageId) {
        Message message = chatIservice.findMessageByIdAndChatroomId(messageId, chatroomId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    @DeleteMapping("/deleteMessage/{messageId}")
    public ResponseEntity<String> deleteMessageByIdAndSender(@PathVariable Integer messageId) {
        chatIservice.deleteMessageByIdAndSender(messageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Message deleted successfully");
    }
}
