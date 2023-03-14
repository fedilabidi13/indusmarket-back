package tn.esprit.usermanagement.services.ForumIservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.ForumEntities.Post;

import java.io.IOException;
import java.util.List;

public interface PostIservice {
    //Post
    ResponseEntity<?> addPost(List<MultipartFile> files, Post post) throws IOException;

    ResponseEntity<?> Update_post(Post post,List<MultipartFile> files, Integer idPost) throws IOException;
    String deletePost(Integer postId) ;
    Post getPostById (Integer id);
    List<Post> Get_all_post() ;
    List<Post> Get_post_by_User(Integer idUser) ;
    List<Post> Searchpost(String ch,Integer id);
}
