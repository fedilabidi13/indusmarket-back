package tn.esprit.usermanagement.servicesImpl;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.entities.Chatroom;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.ChatroomRepo;
import tn.esprit.usermanagement.repositories.MessageRepository;
import tn.esprit.usermanagement.repositories.UserRepo;

@Service
public class ChatService {
    @Autowired
    ChatroomRepo chatroomRepo;
    @Autowired
    UserRepo ur;
    @Autowired
    MessageRepository mr;

    public Chatroom findchat(Integer idsender, Integer idreciver) {
        int x = 0;
        Chatroom cht = new Chatroom();
        for (Chatroom ch : chatroomRepo.findAll()) {
            if (ch.getReciver().getId() == idreciver && ch.getSender().getId() == idsender || ch.getReciver().getId() == idsender && ch.getSender().getId() == idreciver) {
                x = 1;
                cht = ch;
            }

        }
        if (x == 1) {
            return cht;
        } else {
            Chatroom newc = new Chatroom();
            newc.setSender(null);
            User s = ur.findById(idsender).orElse(null);
            User r = ur.findById(idreciver).orElse(null);
            newc.setReciver(r);
            newc.setSender(s);

            return chatroomRepo.save(newc);

        }
    }


/*	public void sendmessage(Message m , Long idchatroom) {
		Chatroom ch = chatroomRepo.findById(idchatroom).orElse(null);
		m.setChat(ch);
		mr.save(m);
		Set<Message> l = ch.getMessages();
		l.add(m);
		ch.setMessages(l);
		chatroomRepo.save(ch);

	}*/

    public Chatroom getConv(Integer idchatroom) {
        Chatroom ch = chatroomRepo.findById(idchatroom).orElse(null);
        return ch;

    }

    public void changecolor(Integer id, String s) {
        Chatroom ch = chatroomRepo.findById(id).orElse(null);
        ch.setColor(s);
        chatroomRepo.save(ch);
    }
}
