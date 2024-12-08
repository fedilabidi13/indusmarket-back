package tn.esprit.usermanagement.services;

import com.google.zxing.WriterException;
import tn.esprit.usermanagement.entities.Ticket;

import java.io.IOException;
import java.util.List;

public interface TicketService {

    public Ticket AddTicketForEventAndAssignToUser(Integer eventId) throws IOException, WriterException;
    public String deleteTicket(Integer idTicket);
    public List<Ticket> ShowUserTickets();

}
