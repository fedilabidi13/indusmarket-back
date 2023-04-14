package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.UserSearchHistory;
@Repository

public interface UserSearchHistoryRepo extends JpaRepository<UserSearchHistory,Integer> {



}
