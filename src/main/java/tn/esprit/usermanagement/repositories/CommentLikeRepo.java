package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.CommentLike;

@Repository
public interface CommentLikeRepo extends JpaRepository<CommentLike, Integer>{

}
