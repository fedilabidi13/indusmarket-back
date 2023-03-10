package tn.esprit.usermanagement.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.*;
import tn.esprit.usermanagement.enumerations.ReactType;

import java.io.IOException;
import java.util.List;


 public interface ForumIservice {
    //Post
     ResponseEntity<?> addPost(List<MultipartFile> files,  Post post, Integer IdUser) throws  IOException;

     ResponseEntity<?> Update_post(Post post,List<MultipartFile> files, Integer idPost, Integer idUser) throws IOException;
     void deletePost(Integer postId) ;
     Post getPostById (Integer id);
     List<Post> Get_all_post() ;
     List<Post> Get_post_by_User(Integer idUser) ;
     List<Post> Searchpost(String ch,Integer id);

    //React For Post
     React addReactToPost(Integer userId, Integer postId, ReactType reactType) ;

     void deleteReact(Integer reactId) ;
     Post getMostReactedPost(ReactType reactType) ;

     List<React> getAllReactsForPost(Post post) ;

    User findMostReactiveUsers();




        //Comment
     ResponseEntity<?> addComment_to_Post(PostComment postComment,List<MultipartFile> files, Integer idPost, Integer idUser) throws IOException;
     ResponseEntity<?> addResponseToComment(Integer commentId,List<MultipartFile> files, PostComment postComment)  throws IOException ;
     ResponseEntity<?> Update_Comment(PostComment postComment, Integer idPostCom, Integer idUser) ;
     ResponseEntity<?> Delete_PostCom(Integer idPostCom, Integer idUser) ;
     List<PostComment> get_post_Comm(Integer idPost) ;
     List<PostComment> get_comm_Comm(Integer idComment) ;

    //React For Comment

     React addReactToComment(Integer userId, Integer commentId, ReactType reactType) ;





    //BadWord
     int Filtrage_bad_word(String ch);
     BadWord addBadWord(BadWord b ) ;
      BadWord  updateBadWord(  BadWord b   );
     void removeBadWord(Integer idBadWord);
     List<BadWord>  GetAllBadWords()  ;
       BadWord   GetBadWord (Integer  idBadWord)  ;









     Boolean existDataForUser(String ch,Integer IdUser);
     UserDataLoad getData(String ch, Integer IdUser);
     void DetctaDataLoad (String ch , Integer idUser);
     List<Pictures> addimages(List<MultipartFile> files) throws IOException ;













    }
