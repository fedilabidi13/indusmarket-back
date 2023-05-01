package tn.esprit.usermanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.WS.ChatServiceWS;
import tn.esprit.usermanagement.WS.ChatroomRepoWS;
import tn.esprit.usermanagement.WS.ChatroomWS;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.UserRepo;


import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/chat")
public class ChatControllerr {
	@Autowired
	ChatServiceWS cs ;
	@Autowired
	UserRepo ur ;
	
	@Autowired
	ChatroomRepoWS cr ;
	
	@GetMapping("/Chatroom/{Idsender}/{idreciver}")
	@ResponseBody
	public ChatroomWS chatfind(@PathVariable("Idsender") Integer Idsender, @PathVariable("idreciver") Integer idreciver) {
	return cs.findchat(Idsender, idreciver);
	}
	
	/*@PostMapping("/send/{idreciver}")
	@ResponseBody
	public void send(@RequestBody Message m,@PathVariable("idreciver") Long idreciver) {
	 cs.sendmessage(m, idreciver);
	}*/
	
	@PostMapping("/getc/{idreciver}")
	@ResponseBody
	public ChatroomWS getcon(@PathVariable("idreciver") Long idreciver) {
	 return cs.getConv(idreciver);
	}
	
	@GetMapping("/ListUser")
	@ResponseBody
	public List<User> getListUser() {

		System.out.println(
				ur.findAll()
		); return ur.findAll();
	}
	
	@GetMapping("/allchat")
	@ResponseBody
	public List<ChatroomWS> getChat() {
	 return cr.findAll();
	}
	
	@PostMapping("/color/{id}")
	@ResponseBody
	public void color(@PathVariable("id") Long id ,@RequestBody String c) {
	 cs.changecolor(id, c);
	}
	
}
