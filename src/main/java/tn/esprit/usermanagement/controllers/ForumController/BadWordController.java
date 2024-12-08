package tn.esprit.usermanagement.controllers.ForumController;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.ForumEntities.BadWord;
import tn.esprit.usermanagement.services.ForumIservice.BadWordIservice;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;

import java.util.List;

@RestController
@RequestMapping("/badWord")
@AllArgsConstructor
@CrossOrigin(origins = "*")

public class BadWordController {
    private final AuthenticationService authenticationService;
    BadWordIservice badWordIservice;
    //BadWord Test Successfully
    @PostMapping("/add")
    @ResponseBody
    public BadWord addBadWord(@ModelAttribute BadWord badWord) {

        return badWordIservice.addBadWord(badWord);
    }

    @PostMapping("/update")
    BadWord  updateBadWord(@ModelAttribute BadWord badWord){

        return    badWordIservice.updateBadWord(badWord);
    }

    @PostMapping ("/delete")
    void deleteBadWord(@RequestParam Integer idBadWord) {

        badWordIservice.removeBadWord(idBadWord);
    }
    @GetMapping("/getAll")
    List<BadWord > getAllBadWords(){

        return badWordIservice.GetAllBadWords();
    }
    @GetMapping("/getById")
    BadWord  getBadWord(@RequestParam Integer idBadWord){

        return  badWordIservice.GetBadWord(idBadWord);
    }

}
