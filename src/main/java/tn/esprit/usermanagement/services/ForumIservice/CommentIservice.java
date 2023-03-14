package tn.esprit.usermanagement.services.ForumIservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.ForumEntities.PostComment;

import java.io.IOException;
import java.util.List;

public interface CommentIservice {
    //Comment
    ResponseEntity<?> addComment_to_Post(PostComment postComment, List<MultipartFile> files, Integer idPost) throws IOException;
    ResponseEntity<?> addResponseToComment(Integer commentId,List<MultipartFile> files, PostComment postComment)  throws IOException ;
    ResponseEntity<?> Update_Comment(PostComment postComment, Integer idPostCom) ;
    ResponseEntity<?> Delete_PostCom(Integer idPostCom) ;
    List<PostComment> get_post_Comm(Integer idPost) ;
    List<PostComment> get_comm_Comm(Integer idComment) ;
    public PostComment getCommentById(Integer idComment) ;

    }
