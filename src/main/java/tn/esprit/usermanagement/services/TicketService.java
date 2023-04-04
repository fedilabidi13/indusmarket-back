package tn.esprit.usermanagement.services;

import com.google.zxing.WriterException;
import tn.esprit.usermanagement.entities.Ticket;

import java.io.IOException;

public interface TicketService {

    public Ticket AddTicketForEventAndAssignToUser(Ticket ticket,Integer eventId) throws IOException, WriterException;
    public String deleteTicket(Integer idTicket);

}
