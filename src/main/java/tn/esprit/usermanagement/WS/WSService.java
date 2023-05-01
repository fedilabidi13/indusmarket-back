package tn.esprit.usermanagement.WS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WSService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationServiceWS notificationService;

    @Autowired
    public WSService(SimpMessagingTemplate messagingTemplate, NotificationServiceWS notificationService) {
        this.messagingTemplate = messagingTemplate;
        this.notificationService = notificationService;
    }

    public void notifyFrontend(final String message) {
        ResponseMessageWS response = new ResponseMessageWS(message);
        notificationService.sendGlobalNotification();

        messagingTemplate.convertAndSend("/chat/messages", response);
    }

    public void notifyUser(final String id, final String message) {
        ResponseMessageWS response = new ResponseMessageWS(message);
        response.setContent(message);
        ChatMessageWS c =new ChatMessageWS() ;
        c.setText(message);
        notificationService.sendPrivateNotification(id);
        messagingTemplate.convertAndSendToUser(id, "/chat/private-messages", c);
    }
}
