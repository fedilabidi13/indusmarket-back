package tn.esprit.usermanagement.controllers;

import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tn.esprit.usermanagement.entities.Ticket;
import tn.esprit.usermanagement.servicesImpl.TicketServiceImpl;

import java.io.IOException;
import java.util.Base64;

@RestController
public class TicketController {
    @Autowired
    TicketServiceImpl ticketService;

    @PostMapping("/AddTicket/{userId}/{eventId}")
    public Ticket AddTicketForEventAndAssignToUser(@RequestBody Ticket ticket, @PathVariable("userId") Integer userId, @PathVariable("eventId") Integer eventId) throws IOException, WriterException {
        return ticketService.AddTicketForEventAndAssignToUser(ticket, userId, eventId);
    }
}
