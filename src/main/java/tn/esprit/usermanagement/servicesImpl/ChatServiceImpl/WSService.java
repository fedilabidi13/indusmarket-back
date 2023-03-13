package tn.esprit.usermanagement.servicesImpl.ChatServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WSService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;



    public void notifyFrontend(final String message) {
        ResponseMessage response = new ResponseMessage(message);
        notificationService.sendGlobalNotification();

        messagingTemplate.convertAndSend("/topic/messages", response);
    }

    public void notifyUser(final String id, final String message) {
        ResponseMessage response = new ResponseMessage(message);

        notificationService.sendPrivateNotification(id);
        messagingTemplate.convertAndSendToUser(id, "/topic/private-messages", response);
    }
}
