package tn.esprit.usermanagement.controllers.ForumController;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.ForumEntities.Post;
import tn.esprit.usermanagement.entities.ForumEntities.React;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.enumerations.ReactType;
import tn.esprit.usermanagement.repositories.PostCommentRepo;
import tn.esprit.usermanagement.repositories.PostRepo;
import tn.esprit.usermanagement.services.ForumIservice.ReactIservice;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;

import java.util.List;

@RestController
@RequestMapping("/react")
@AllArgsConstructor
@CrossOrigin(origins = "*")

public class ReactController {
    AuthenticationService authenticationService;
    ReactIservice reactIservice;
    PostRepo postRepo;
    PostCommentRepo postCommentRepo;
    //React Post


    @PostMapping("post/add")
    public ResponseEntity<React> addReactToPost(@RequestParam Integer idPost, @RequestParam String reactType) {
        React react = reactIservice.addReactToPost(idPost, ReactType.valueOf(reactType));
        return ResponseEntity.ok(react);
    }


    @PostMapping("/deleteReact")
    public ResponseEntity<String> deleteReact(@RequestParam Integer idReact) {
        reactIservice.deleteReact(idReact);
        return new ResponseEntity<>("React with id " + idReact + " has been deleted successfully", HttpStatus.OK);
    }

    @GetMapping("post/mostReacted")
    public Post postMostReacted(@RequestParam(required = false) ReactType reactType) {
        if (reactType== null)
        {
            return reactIservice.getMostReactedPost(null);
        }
        else {
            return reactIservice.getMostReactedPost(reactType);
        }
    }

    @GetMapping("post/getAll")
    public List<React> getAllReactsForPost(@RequestParam Integer idPost) {

        return reactIservice.getAllReactsForPost(postRepo.getReferenceById(idPost));
    }
    @GetMapping("post/mostReactiveUser")
    public ResponseEntity<?> getMostReactiveUsers() {
        User user = reactIservice.findMostReactiveUsers();
        if (user == null) {
            return ResponseEntity.ok("there is not enough reactions! ");
        } else {
            return ResponseEntity.ok(user.getEmail());
        }
    }

    //React For Comment
    @PostMapping("comment/add")
    public ResponseEntity<React> addReactToComment( @RequestParam Integer idComment, @RequestParam ReactType reactType) {
        React react = reactIservice.addReactToComment(idComment, reactType);
        return ResponseEntity.ok(react);
    }
    @GetMapping("comment/getAll")
    public List<React> getAllReactsForComment(@RequestParam Integer idComment) {

        return reactIservice.getAllReactsForComment(postCommentRepo.getReferenceById(idComment));
    }

}
