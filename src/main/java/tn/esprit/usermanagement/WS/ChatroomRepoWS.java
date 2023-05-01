package tn.esprit.usermanagement.WS;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatroomRepoWS extends JpaRepository<ChatroomWS, Long>{

}
