package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.PostComment;

@Repository
public interface PostCommentRepo extends JpaRepository<PostComment, Integer>{

}
