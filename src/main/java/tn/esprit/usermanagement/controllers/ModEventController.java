package tn.esprit.usermanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.servicesImpl.EventServiceImpl;

@RestController
@RequestMapping("/mod/events")
public class ModEventController {
    @Autowired
    EventServiceImpl eventService;
    @PutMapping("accepEvent/{eventId}")
    public void AcceptEvent(@PathVariable("eventId") Integer eventId){
        eventService.AcceptEvent(eventId);
    }
    @DeleteMapping("/deleteEvent/{eventId}")
    public void deleteEvent(@PathVariable("eventId") Integer eventId){
        eventService.ModDeleteEvent(eventId);
    }
}
