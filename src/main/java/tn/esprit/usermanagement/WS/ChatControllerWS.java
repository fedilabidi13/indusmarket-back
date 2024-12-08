package tn.esprit.usermanagement.WS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// The chat message-handling Controller
@RestController
@RequestMapping("/chatt")
@CrossOrigin(origins = "*")
public class ChatControllerWS {
	@Autowired
	ChatmessageRepoWS mr;
	@Autowired
	ChatroomRepoWS cr;
	
    // mapped to handle chat messages to the /sendmsg destination
    @MessageMapping("/sendmsg")
    // the return value is broadcast to all subscribers of /chat/messages
    @SendTo("/chat/messages")
    public ChatMessageWS chat(ChatMessageWS message) throws Exception {
        Thread.sleep(1000); // simulated delay
        ChatroomWS ch1 = cr.findById(message.idchat).orElse(null);
        message.setChatWS(ch1);
        mr.save(message);
        return new ChatMessageWS(message.messageId,message.getText(), message.getUsername(), message.getAvatar(),message.getSenderWS(),message.idchat,message.chatWS);
    }
    
}
