package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.usermanagement.entities.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepo extends JpaRepository<Event,Integer> {
    @Query("select e from Event e where e.endDate<?1 ")
    List<Event> findByEndDateIsBefore(LocalDateTime limite);
    @Query("select e from Event e order by e.startDate asc ")
    List<Event> OrderByStartDate();
}
