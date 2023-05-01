package tn.esprit.usermanagement.WS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceWS {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public NotificationServiceWS(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendGlobalNotification() {
        ResponseMessageWS message = new ResponseMessageWS("Global Notification");

        messagingTemplate.convertAndSend("/chat/global-notifications", message);
    }

    public void sendPrivateNotification(final String userId) {
        ResponseMessageWS message = new ResponseMessageWS("Private Notification");

        messagingTemplate.convertAndSendToUser(userId,"/chat/private-notifications", message);
    }
}
