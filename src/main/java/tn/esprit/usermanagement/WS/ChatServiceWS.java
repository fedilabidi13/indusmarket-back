package tn.esprit.usermanagement.WS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.UserRepo;

import java.util.Set;

@Service
public class ChatServiceWS {
	@Autowired
	ChatroomRepoWS chatroomRepo;
	@Autowired
	UserRepo ur;
	@Autowired
	MessageRepositoryWS mr;
	public ChatroomWS findchat(Integer idsender , Integer idreciver) {
		int x = 0;
		ChatroomWS cht =  new ChatroomWS();
		for (ChatroomWS ch : chatroomRepo.findAll()) {
			if (ch.getReciverWS().getId() == idreciver && ch.getSenderWS().getId() == idsender || ch.getReciverWS().getId() == idsender && ch.getSenderWS().getId() == idreciver) {
			x= 1;
			cht=ch;}
			
		}
		if (x== 1) { return cht;}
		else {
			ChatroomWS newc =  new ChatroomWS();
			newc.setSenderWS(null);
			User s = ur.findById(idsender).orElse(null);
			User r = ur.findById(idreciver).orElse(null);
			newc.setReciverWS(r);
			newc.setSenderWS(s);
			
			return chatroomRepo.save(newc);

		}
	}
	
	
	/*public void sendmessage(MessageWS m , Long idchatroom) {
		ChatroomWS ch = chatroomRepo.findById(idchatroom).orElse(null);
		m.setChatWS(ch);
		mr.save(m);
		Set<MessageWS> l = ch.getMessages();
		l.add(m);
		ch.setMessages(l);
		chatroomRepo.save(ch);
		
	}*/
	
	public ChatroomWS getConv(Long idchatroom) {
		ChatroomWS ch = chatroomRepo.findById(idchatroom).orElse(null);
		return ch;
		
	}
	public void changecolor(Long id, String s){
		ChatroomWS ch = chatroomRepo.findById(id).orElse(null);
		ch.setColor(s);
		chatroomRepo.save(ch);
	}

}
