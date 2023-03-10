package tn.esprit.usermanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.usermanagement.entities.ChatMessage;
import tn.esprit.usermanagement.servicesImpl.WSService;

@RestController
public class WSController {

    @Autowired
    private WSService service;

    @PostMapping("/send-message")
    public void sendMessage(@RequestBody final ChatMessage message) {
        service.notifyFrontend(message.getText());
    }

    @PostMapping("/send-private-message/{id}")
    public void sendPrivateMessage(@PathVariable final String id,
                                   @RequestBody final ChatMessage message) {
        service.notifyUser(id, message.getText());
    }
}

