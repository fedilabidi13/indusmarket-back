package tn.esprit.usermanagement.controllers.ForumController;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.ForumEntities.PostComment;
import tn.esprit.usermanagement.services.ForumIservice.CommentIservice;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {
    CommentIservice commentIservice;

    //Comment
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addComment_to_Post(@ModelAttribute PostComment postComment, @RequestParam Integer idPost, @RequestParam("files") List<MultipartFile> files) throws IOException {

        return  commentIservice.addComment_to_Post(postComment,files,idPost);


    }
    @PostMapping("/addResponse")
    public ResponseEntity<?> addResponseToComment(@RequestParam Integer idComment, @ModelAttribute PostComment response, @RequestParam("files")List<MultipartFile> files) throws IOException {
        return commentIservice.addResponseToComment(idComment,files, response);
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<?> Update_Comment(@ModelAttribute PostComment postComment, @RequestParam Integer idComment) {
        return commentIservice.Update_Comment(postComment,idComment);
    }
    @PostMapping("/delete")
    public ResponseEntity<?> Delete_PostCom( @RequestParam Integer idComment) {
        return commentIservice.Delete_PostCom(idComment);
    }
    @GetMapping("/getAll")
    public List<PostComment> get_post_Comm(@RequestParam Integer idPost ){
        return commentIservice.get_post_Comm(idPost);
    }

    @GetMapping("/getReply")
    public  List<PostComment> Get_comm_Comm( @RequestParam Integer idComment ){
        return commentIservice.get_comm_Comm (idComment);
    }
    @GetMapping("/getById")
    public ResponseEntity<PostComment> getCommentById(@RequestParam Integer idComment) {
        PostComment comment = commentIservice.getCommentById(idComment);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }


}
