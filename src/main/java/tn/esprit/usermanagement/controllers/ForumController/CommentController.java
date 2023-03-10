package tn.esprit.usermanagement.controllers.ForumController;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.PostComment;
import tn.esprit.usermanagement.services.ForumIservice.CommentIservice;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {
    CommentIservice commentIservice;

    //Comment
    @PostMapping("comment/add")
    @ResponseBody
    public ResponseEntity<?> addComment_to_Post(@ModelAttribute PostComment postComment, @RequestParam Integer postId, @RequestParam("files") List<MultipartFile> files) throws IOException {

        return  commentIservice.addComment_to_Post(postComment,files,postId);


    }
    @PostMapping("comment/addResponse")
    public ResponseEntity<?> addResponseToComment(@RequestParam Integer commentId, @ModelAttribute PostComment response, @RequestParam("files")List<MultipartFile> files) throws IOException {
        return commentIservice.addResponseToComment(commentId,files, response);
    }

    @PostMapping("comment/update")
    @ResponseBody
    public ResponseEntity<?> Update_Comment(@ModelAttribute PostComment postComment, @RequestParam Integer idPostCom) {
        return commentIservice.Update_Comment(postComment,idPostCom);
    }
    @PostMapping("comment/delete")
    public ResponseEntity<?> Delete_PostCom( @RequestParam Integer idPostCom) {
        return commentIservice.Delete_PostCom(idPostCom);
    }
    @GetMapping("comment/getAll")
    public List<PostComment> get_post_Comm(@RequestParam Integer idPost ){
        return commentIservice.get_post_Comm(idPost);
    }

    @GetMapping("comment/getReply")
    public  List<PostComment> Get_comm_Comm( @RequestParam Integer idComment ){
        return commentIservice.get_comm_Comm (idComment);
    }

}
