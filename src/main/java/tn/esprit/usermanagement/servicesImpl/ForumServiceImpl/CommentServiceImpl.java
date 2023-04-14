package tn.esprit.usermanagement.servicesImpl.ForumServiceImpl;

import com.cloudinary.Cloudinary;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.ForumEntities.Media;
import tn.esprit.usermanagement.entities.ForumEntities.Post;
import tn.esprit.usermanagement.entities.ForumEntities.PostComment;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.*;
import tn.esprit.usermanagement.services.ForumIservice.CommentIservice;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentIservice {
    PostCommentRepo postCommentRepo;
    PostRepo postRepo;
    UserRepo userRepo;
    ReactRepo reactRepository;
    AuthenticationService authenticationService;
    private DataLoadServiceImpl dataLoadService;
    private BadWordServiceImpl badWordService;
    Cloudinary cloudinary;
    MediaRepo mediaRepo;

    //Comment
    public ResponseEntity<?> addComment_to_Post(PostComment postComment, List<MultipartFile> files, Integer idPost) throws IOException {
        Post p = postRepo.getReferenceById(idPost);
        dataLoadService.DetctaDataLoad(postComment.getCommentBody(),authenticationService.currentlyAuthenticatedUser().getId());
        PostComment comment = new PostComment();
        if (badWordService.Filtrage_bad_word(postComment.getCommentBody()) == 0) {

            comment.setCommentBody(postComment.getCommentBody());
            comment.setCommentedAt(postComment.getCommentedAt());
            if (files==null||files.isEmpty()) {
                comment.setMedias(null);
                comment.setPost(p);
                comment.setUser(authenticationService.currentlyAuthenticatedUser());
                comment.setCommentedAt(LocalDateTime.now());
                postCommentRepo.save(comment);
                return ResponseEntity.ok().body(comment.getCommentBody());
            }
            else{
                List<Media> mediaList = new ArrayList<>();
                for (MultipartFile multipartFile : files) {
                    Media media = new Media();
                    String url = cloudinary.uploader()
                            .upload(multipartFile.getBytes(),
                                    Map.of("public_id", UUID.randomUUID().toString()))
                            .get("url")
                            .toString();
                    media.setImagenUrl(url);
                    media.setName(multipartFile.getName());
                    mediaList.add(media);
                }
                mediaRepo.saveAll(mediaList);
                comment.setMedias(mediaList);
                comment.setPost(p);
                comment.setUser(authenticationService.currentlyAuthenticatedUser());
                comment.setCommentedAt(LocalDateTime.now());
                postCommentRepo.save(comment);
                return ResponseEntity.ok().body(comment.getCommentBody());
            }
        }else

            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Bads Word Detected");
    }
    public ResponseEntity<?> addResponseToComment(Integer commentId,List<MultipartFile> files, PostComment postComment)  throws IOException{
        PostComment comment = new PostComment();
        PostComment originalComment = postCommentRepo.getReferenceById(commentId);
        if (badWordService.Filtrage_bad_word(postComment.getCommentBody()) == 0) {
            comment.setPostComment(originalComment);
            comment.setUser(authenticationService.currentlyAuthenticatedUser());
            comment.setCommentBody(postComment.getCommentBody());
            comment.setCommentedAt(postComment.getCommentedAt());
            comment.setPost(postComment.getPost());
            List<Media> mediaList = new ArrayList<>();
            for (MultipartFile multipartFile : files) {
                Media media = new Media();
                String url = cloudinary.uploader()
                        .upload(multipartFile.getBytes(),
                                Map.of("public_id", UUID.randomUUID().toString()))
                        .get("url")
                        .toString();
                media.setImagenUrl(url);
                media.setName(multipartFile.getName());
                mediaList.add(media);
            }
            mediaRepo.saveAll(mediaList);
            comment.setMedias(mediaList);
            comment.setCommentedAt(LocalDateTime.now())	;
            postCommentRepo.save(comment);
            return ResponseEntity.ok().body(comment.getCommentBody());
        } else {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Bads Word Detected");
        }


    }

    /*public ResponseEntity<?> Update_Comment(PostComment postComment, Integer idPostCom) {
        if (postComment.getUser().getId()==authenticationService.currentlyAuthenticatedUser().getId()) {
            if (postCommentRepo.existsById(idPostCom)) {
                PostComment postCom1 = postCommentRepo.findById(idPostCom)
                        .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

                postCom1.setCommentBody(postComment.getCommentBody());
                postCommentRepo.save(postCom1);
                return ResponseEntity.ok().body("comment updated");

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment Not Found");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("you are not the owner of this comment");

    }*/
    public ResponseEntity<?> Update_Comment(PostComment postComment, Integer idPostCom) {
        // Get the currently authenticated user
        User currentUser = authenticationService.currentlyAuthenticatedUser();

        // Check if the user is the owner of the comment
        if (postCommentRepo.existsById(idPostCom)) {
            PostComment postCom1 = postCommentRepo.findById(idPostCom)
                    .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

            if (postCom1.getUser().getId() == currentUser.getId()) {
                // Update the comment
                postCom1.setCommentBody(postComment.getCommentBody());
                postCommentRepo.save(postCom1);
                return ResponseEntity.ok().body("comment updated");
            } else {
                // User is not the owner of the comment
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the owner of this comment");
            }
        } else {
            // Comment not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found");
        }
    }






    public ResponseEntity<?> Delete_PostCom(Integer idPostCom) {
        if (postCommentRepo.existsById(idPostCom)) {
            PostComment postCom1 = postCommentRepo.findById(idPostCom)
                    .orElseThrow(() -> new EntityNotFoundException("post not found"));
            User user = authenticationService.currentlyAuthenticatedUser();
            if (postCom1.getUser().equals(user)) {
                postCommentRepo.delete(postCom1);
                return ResponseEntity.ok().body("Delete success");
            } else {
                return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("No permission to delete this post");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("post Not Founf");
        }
    }
    public List<PostComment> get_post_Comm(Integer idPost) {

        return postCommentRepo.findByPost(postRepo.getReferenceById(idPost));

    }

    public List<PostComment> get_comm_Comm(Integer idComment) {
        return postCommentRepo.findByPostComment(postCommentRepo.getReferenceById(idComment));
    }
    public PostComment getCommentById(Integer idComment) {
        return postCommentRepo.findById(idComment)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
    }

}
