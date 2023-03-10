package tn.esprit.usermanagement.repositories;
import org.attoparser.dom.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.Post;
import tn.esprit.usermanagement.entities.PostComment;
import tn.esprit.usermanagement.entities.React;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.enumerations.ReactType;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactRepo extends JpaRepository<React, Integer>{
    //Optional<React> findByUserAndPost(User user, Post post);
    List<React> findByTypeAndPost(ReactType type, Post post);
   // long countByTypeAndPost(ReactType type, Post post);
    List<React> findByPost(Post post);
   // @Query("SELECT r FROM React r JOIN FETCH r.user WHERE r.post.id = :postId")
   //List<React> findAllByPostIdWithUser( Integer postId);
    //List<React> findByPostIdAndUserIdAndType(Integer postId, Integer userId, ReactType type);
    List<React> findByUser(User user);
    React findByPostAndUser( Post post, User user);
    React findByCommentAndUser(PostComment comment, User user);

    Integer countByUser(User user);

    Integer countByPostIdAndType(Integer postId, ReactType type);

}
