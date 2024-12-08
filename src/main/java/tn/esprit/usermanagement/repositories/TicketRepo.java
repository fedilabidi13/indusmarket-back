package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.usermanagement.entities.Event;
import tn.esprit.usermanagement.entities.Ticket;
import tn.esprit.usermanagement.entities.User;

import java.util.List;

public interface TicketRepo extends JpaRepository<Ticket,Integer> {
Ticket findByUserAndEvent(User user, Event event);
List<Ticket> findByUser(User user);
}
