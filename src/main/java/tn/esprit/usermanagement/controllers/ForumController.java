package tn.esprit.usermanagement.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.*;
import tn.esprit.usermanagement.enumerations.ReactType;
import tn.esprit.usermanagement.repositories.PostRepo;
import tn.esprit.usermanagement.services.ForumIservice;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/forum")
@AllArgsConstructor
public class ForumController {
    private final AuthenticationService authenticationService;
    private PostRepo postRepo;
    ForumIservice forumIservice;
    //Post
    @PostMapping("/post/add")
    @ResponseBody
    public ResponseEntity<?> addPost(@ModelAttribute Post post,@RequestParam("files")List<MultipartFile> files) throws IOException {
        Integer idUser = authenticationService.currentlyAuthenticatedUser().getId();

        post.setCreatedAt(LocalDateTime.now());
        return forumIservice.addPost(files,post,idUser);
    }
    @PostMapping("/post/update")
    @ResponseBody
    public ResponseEntity<?> Update_Post(@ModelAttribute Post post,@RequestParam("files") List<MultipartFile> files,  @RequestParam Integer IdPost) throws IOException {
        Integer idUser = authenticationService.currentlyAuthenticatedUser().getId();
        return forumIservice.Update_post(post,files,IdPost,idUser);
    }
    @PostMapping("/post/delete")
    public ResponseEntity<String> deletePost(@RequestParam Integer IdPost) {
        forumIservice.deletePost(IdPost);
        return new ResponseEntity<>("post with id " + IdPost + " has been deleted successfully", HttpStatus.OK);
    }


    @GetMapping("/post/findAll")
    public List<Post> Get_all_post(){

        return forumIservice.Get_all_post();
    }
    @GetMapping("/post/findByUser")
    public List<Post> Get_post_by_User(@RequestParam Integer idUser){

        return forumIservice.Get_post_by_User(idUser);
    }
    @GetMapping("/post/findById")
    public Post Get_Post_Details(@RequestParam Integer postId) {
        return forumIservice.getPostById(postId);

    }
    @GetMapping("/post/search")
    public  List<Post> adversting_bydata(@RequestParam String ch){
        Integer idUser = authenticationService.currentlyAuthenticatedUser().getId();
        return forumIservice.Searchpost(ch,idUser);
    }
    //React Post


    @PostMapping("post/addReact")
    public ResponseEntity<React> addReact( @RequestParam Integer postId, @RequestParam String reactType) {
        Integer userId = authenticationService.currentlyAuthenticatedUser().getId();
        React react = forumIservice.addReactToPost(userId, postId, ReactType.valueOf(reactType));
        return ResponseEntity.ok(react);
    }


    @PostMapping("post/deleteReact")
    public ResponseEntity<String> deleteReact(@RequestParam Integer reactId) {
        forumIservice.deleteReact(reactId);
        return new ResponseEntity<>("React with id " + reactId + " has been deleted successfully", HttpStatus.OK);
    }

    @GetMapping("post/mostReacted")
    public Post postMostReacted(@RequestParam(required = false) ReactType reactType) {
        if (reactType== null)
        {
            return forumIservice.getMostReactedPost(null);
        }
        else {
            return forumIservice.getMostReactedPost(reactType);
        }
    }

    @GetMapping("post/getAllReactsForPost")
    public List<React> getAllReactsForPost(@RequestParam Integer postId) {

        return forumIservice.getAllReactsForPost(postRepo.getReferenceById(postId));
    }

    @GetMapping("post/mostReactiveUser")
    public ResponseEntity<?> getMostReactiveUsers() {
        User user = forumIservice.findMostReactiveUsers();
        if (user == null) {
            return ResponseEntity.ok("there is not enough reactions! ");
        } else {
            return ResponseEntity.ok(user.getEmail());
        }
    }
    //Comment
    @PostMapping("comment/add")
    @ResponseBody
    public ResponseEntity<?> addComment_to_Post(@ModelAttribute PostComment postComment, @RequestParam Integer postId, @RequestParam("files")List<MultipartFile> files) throws IOException {
        Integer idUser = authenticationService.currentlyAuthenticatedUser().getId();

        postComment.setCommentedAt(LocalDateTime.now())	;

        return  forumIservice.addComment_to_Post(postComment,files,postId,idUser);


    }
    @PostMapping("comment/addResponse")
    public ResponseEntity<?> addResponseToComment(@RequestParam Integer commentId, @ModelAttribute PostComment response, @RequestParam("files")List<MultipartFile> files) throws IOException {
        response.setCommentedAt(LocalDateTime.now())	;
        return forumIservice.addResponseToComment(commentId,files, response);
    }

    @PostMapping("comment/update")
    @ResponseBody
    public ResponseEntity<?> Update_Comment(@ModelAttribute PostComment postComment, @RequestParam Integer idPostCom) {
        Integer idUser = authenticationService.currentlyAuthenticatedUser().getId();
        return forumIservice.Update_Comment(postComment,idPostCom,idUser);
    }
    @PostMapping("comment/delete")
    public ResponseEntity<?> Delete_PostCom( @RequestParam Integer idPostCom) {
        Integer idUser = authenticationService.currentlyAuthenticatedUser().getId();
        return forumIservice.Delete_PostCom(idPostCom,idUser);
    }
    @GetMapping("comment/getAll")
    public List<PostComment> get_post_Comm(@RequestParam Integer idPost ){
        return forumIservice.get_post_Comm(idPost);
    }

    @GetMapping("comment/getReply")
    public  List<PostComment> Get_comm_Comm( @RequestParam Integer idComment ){
        return forumIservice.get_comm_Comm (idComment);
    }
    //React For Comment
    @PostMapping("comment/addReact")
    public ResponseEntity<React> addReactToComment( @RequestParam Integer commentId, @RequestParam ReactType reactType) {
        Integer userId = authenticationService.currentlyAuthenticatedUser().getId();
        React react = forumIservice.addReactToComment(userId, commentId, reactType);
        return ResponseEntity.ok(react);
    }


    //BadWord Test Successfully
    @PostMapping("badWord/add")
    @ResponseBody
    public BadWord addBadWord(@ModelAttribute BadWord b) {

        return forumIservice.addBadWord(b);
    }

    @PostMapping("badWord/update")
    BadWord  updateBadWord(@ModelAttribute BadWord badWord){

        return    forumIservice.updateBadWord(badWord);
    }

    @PostMapping ("badWord/delete")
    void deleteBadWord(@RequestParam Integer idBadWord) {

        forumIservice.removeBadWord(idBadWord);
    }
    @GetMapping("badWord/getAll")
    List<BadWord >getAllBadWords(){

        return forumIservice.GetAllBadWords();
    }
    @GetMapping("badWord/getById")
    BadWord  getBadWord(@RequestParam Integer idBadWord){

        return  forumIservice.GetBadWord(idBadWord);
    }


}
