package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.usermanagement.entities.Ticket;
public interface TicketRepo extends JpaRepository<Ticket,Integer> {
}
