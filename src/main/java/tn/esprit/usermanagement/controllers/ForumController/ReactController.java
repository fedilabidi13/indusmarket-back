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
public class ReactController {
    AuthenticationService authenticationService;
    ReactIservice reactIservice;
    PostRepo postRepo;
    PostCommentRepo postCommentRepo;
    //React Post


    @PostMapping("post/addReact")
    public ResponseEntity<React> addReact(@RequestParam Integer postId, @RequestParam String reactType) {
        React react = reactIservice.addReactToPost(postId, ReactType.valueOf(reactType));
        return ResponseEntity.ok(react);
    }


    @PostMapping("/deleteReact")
    public ResponseEntity<String> deleteReact(@RequestParam Integer reactId) {
        reactIservice.deleteReact(reactId);
        return new ResponseEntity<>("React with id " + reactId + " has been deleted successfully", HttpStatus.OK);
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

    @GetMapping("post/getAllReactsForPost")
    public List<React> getAllReactsForPost(@RequestParam Integer postId) {

        return reactIservice.getAllReactsForPost(postRepo.getReferenceById(postId));
    }
    @GetMapping("/post/{postId}/owner")
    ResponseEntity<?> getAllReactionsByPostAndOwner(@PathVariable Integer postId) {
        if (reactIservice.getAllReactionsByPostIdAndOwner(postId).isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(reactIservice.getAllReactionsByPostIdAndOwner(postId), HttpStatus.OK);
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
    @PostMapping("comment/addReact")
    public ResponseEntity<React> addReactToComment( @RequestParam Integer commentId, @RequestParam ReactType reactType) {
        React react = reactIservice.addReactToComment(commentId, reactType);
        return ResponseEntity.ok(react);
    }
    @GetMapping("comment/getAllReactsForComment")
    public List<React> getAllReactsForComment(@RequestParam Integer commentId) {

        return reactIservice.getAllReactsForComment(postCommentRepo.getReferenceById(commentId));
    }
    @GetMapping("/comment/{commentId}/owner/{userId}")
    ResponseEntity<?> getAllReactionsByCommentAndOwner(@PathVariable Integer commentId, @PathVariable Integer userId) {
        if (reactIservice.getAllReactionsByCommentIdAndOwner(commentId, userId).isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(reactIservice.getAllReactionsByCommentIdAndOwner(commentId, userId), HttpStatus.OK);
    }


}
