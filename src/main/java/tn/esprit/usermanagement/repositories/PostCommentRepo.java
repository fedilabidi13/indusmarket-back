package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.ForumEntities.Post;
import tn.esprit.usermanagement.entities.ForumEntities.PostComment;

import java.util.List;

@Repository
public interface PostCommentRepo extends JpaRepository<PostComment, Integer>{
    List<PostComment> findByPost(Post post);
    List<PostComment> findByPostComment(PostComment postComment);




}
