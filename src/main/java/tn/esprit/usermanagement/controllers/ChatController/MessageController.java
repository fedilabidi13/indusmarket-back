package tn.esprit.usermanagement.controllers.ChatController;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.ChatEntities.Message;
import tn.esprit.usermanagement.entities.ForumEntities.Post;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.services.MessageIservice;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;

import java.io.IOException;
import java.util.List;
@RestController
@AllArgsConstructor
public class MessageController {
    MessageIservice messageIservice;
    private AuthenticationService authenticationService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestParam String body, @RequestParam String reciverEmail, @RequestParam("files") List<MultipartFile> files) throws IOException {
        return messageIservice.sendMessage(body, reciverEmail,files);
    }

    @GetMapping("/get/{senderId}/{receiverId}")
    public ResponseEntity<List<Message>> getMessagesBySenderAndReceiver(@PathVariable Integer senderId,
                                                                        @PathVariable Integer receiverId) {
        User sender = new User();
        sender.setId(senderId);
        User receiver = new User();
        receiver.setId(receiverId);
        List<Message> messages = messageIservice.getMessagesBySenderAndReceiver(sender, receiver);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
    @GetMapping("/searchMessage")
    public  List<Message> SearchMesssage(@RequestParam String ch){
        Integer idUser = authenticationService.currentlyAuthenticatedUser().getId();
        return messageIservice.Searchmessage(ch,idUser);
    }
}
