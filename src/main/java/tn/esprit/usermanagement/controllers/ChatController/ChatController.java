package tn.esprit.usermanagement.controllers.ChatController;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.ChatEntities.Chatroom;
import tn.esprit.usermanagement.entities.ChatEntities.Message;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.ChatIservice;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/chatroom")
@AllArgsConstructor
public class ChatController {
ChatIservice chatIservice;
UserRepo userRepo;
AuthenticationService authenticationService;
    @PostMapping("/add")
    public ResponseEntity<Chatroom> addChatroom(@ModelAttribute Chatroom chatroom) {
        Chatroom savedChatroom = chatIservice.createChatroom(chatroom);
        return new ResponseEntity<>(savedChatroom, HttpStatus.CREATED);
    }
    @PostMapping("/join")
    public ResponseEntity<?> joinChatroom(@RequestParam String codeRoom) {
        Chatroom chatroom = chatIservice.findByCodeRoom(codeRoom);
        if (chatroom == null) {
            return ResponseEntity.notFound().build();
        }
        chatroom = chatIservice.addUserToChatroom(chatroom);
        return ResponseEntity.ok(chatroom);
    }
    @PostMapping("/sendMessageToChatroom")
    public ResponseEntity<String> sendMessageToChatroom(@RequestParam Integer chatroomId,
                                                        @RequestParam String message,
                                                        @RequestParam List<MultipartFile> files) throws IOException {
        chatIservice.sendMessageToChatroom(message, chatroomId, files);
        return ResponseEntity.ok(message);

    }
    @GetMapping("/getAllMessages")
    public ResponseEntity<List<Message>> getAllMessagesByChatroomIdSortedByDate(@RequestParam Integer chatroomId) {
        List<Message> messages = chatIservice.getAllMessagesByChatroomIdSortedByDate(chatroomId);
        return ResponseEntity.ok(messages);
    }
    @GetMapping("/getMessage")
    public ResponseEntity<Message> getMessage(@RequestParam Integer chatroomId, @RequestParam Integer messageId) {
        Message message = chatIservice.findMessageByIdAndChatroomId(messageId, chatroomId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
