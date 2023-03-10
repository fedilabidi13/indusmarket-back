package tn.esprit.usermanagement.controllers.ForumController;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.ForumEntities.Post;
import tn.esprit.usermanagement.services.ForumIservice.PostIservice;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/post")
@AllArgsConstructor
public class PostController {
    private final AuthenticationService authenticationService;
    PostIservice postIservice;
    //Post
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addPost(@ModelAttribute Post post, @RequestParam("files") List<MultipartFile> files) throws IOException {
        return postIservice.addPost(files,post);
    }
    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<?> Update_Post(@ModelAttribute Post post,@RequestParam("files") List<MultipartFile> files,  @RequestParam Integer IdPost) throws IOException {
        return postIservice.Update_post(post,files,IdPost);
    }
    @PostMapping("/delete")
    public ResponseEntity<String> deletePost(@RequestParam Integer IdPost) {
        postIservice.deletePost(IdPost);
        return new ResponseEntity<>("post with id " + IdPost + " has been deleted successfully", HttpStatus.OK);
    }


    @GetMapping("/findAll")
    public List<Post> Get_all_post(){

        return postIservice.Get_all_post();
    }
    @GetMapping("/findByUser")
    public List<Post> Get_post_by_User(@RequestParam Integer idUser){

        return postIservice.Get_post_by_User(idUser);
    }
    @GetMapping("/findById")
    public Post Get_Post_Details(@RequestParam Integer postId) {
        return postIservice.getPostById(postId);

    }
    @GetMapping("/search")
    public  List<Post> adversting_bydata(@RequestParam String ch){
        Integer idUser = authenticationService.currentlyAuthenticatedUser().getId();
        return postIservice.Searchpost(ch,idUser);
    }
}
