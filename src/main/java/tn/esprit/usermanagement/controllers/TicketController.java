package tn.esprit.usermanagement.controllers;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.Ticket;
import tn.esprit.usermanagement.servicesImpl.TicketServiceImpl;
import java.io.IOException;

@RestController
@RequestMapping("/tickets")
@CrossOrigin(origins = "*")

public class TicketController {
    @Autowired
    TicketServiceImpl ticketService;

    //http://localhost:8085/tickets/addTicket/{eventId}
    @PostMapping("/addTicket/{eventId}")
    public Ticket AddTicketForEventAndAssignToUser(@RequestBody Ticket ticket, @PathVariable("eventId") Integer eventId) throws IOException, WriterException {
        return ticketService.AddTicketForEventAndAssignToUser(ticket,eventId);
    }
    @DeleteMapping("/delete/{idTicket}")
    public String deleteTicket(@PathVariable("idTicket") Integer idTicket){
        return ticketService.deleteTicket(idTicket);
    }

}
