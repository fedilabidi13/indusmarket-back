package tn.esprit.usermanagement.controllers.ChatController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.ChatEntities.Chatroom;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.ChatroomRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.servicesImpl.ChatServiceImpl.ChatService;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    ChatService cs ;
    @Autowired
    UserRepo ur ;

    @Autowired
    ChatroomRepo cr ;

    @GetMapping("/Chatroom/{Idsender}/{idreciver}")
    @ResponseBody
    public Chatroom chatfind(@PathVariable("Idsender") Integer Idsender, @PathVariable("idreciver") Integer idreciver) {
        return cs.findchat(Idsender, idreciver);
    }

	/*@PostMapping("/send/{idreciver}")
	@ResponseBody
	public void send(@RequestBody Message m,@PathVariable("idreciver") Long idreciver) {
	 cs.sendmessage(m, idreciver);
	}*/

    @PostMapping("/getc/{idreciver}")
    @ResponseBody
    public Chatroom getcon(@PathVariable("idreciver") Integer idreciver) {
        return cs.getConv(idreciver);
    }

    @GetMapping("/ListUser")
    @ResponseBody
    public List<User> getListUser() {
        return ur.findAll();
    }

    @GetMapping("/allchat")
    @ResponseBody
    public List<Chatroom> getChat() {
        return cr.findAll();
    }

    /*@PostMapping("/color/{id}")
    @ResponseBody
    public void color(@PathVariable("id") Long id ,@RequestBody String c) {
        cs.changecolor(id, c);
    }*/

}
