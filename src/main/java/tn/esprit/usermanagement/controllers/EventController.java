package tn.esprit.usermanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Event;
import tn.esprit.usermanagement.repositories.EventRepo;
import tn.esprit.usermanagement.servicesImpl.EventServiceImpl;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
@Autowired
EventServiceImpl eventService;
@Autowired
    EventRepo eventRepo;

    //http://localhost:8085/events/showEvents

    @GetMapping("/showEvents")
    public List<Event> ShowEvents(){
        return eventService.ShowEvents();
    }
    //http://localhost:8085/events/ShowEventbyUser
    @GetMapping("/ShowEventbyUser")
    public List<Event> ShowEventbyUser(){
        return eventService.ShowEventbyUser();
    }
    //http://localhost:8085/events/AddEventWithPictureAndAssignToUser
    @PostMapping("/AddEventWithPictureAndAssignToUser")
    public Event AddEventWithPictureAndAssignToUser(@ModelAttribute Event event, @RequestParam("files") List<MultipartFile> files) throws IOException{
        return eventService.AddEventWithPictureAndAssignToUser(event.getAdresse(),event,files);
    }
    //http://localhost:8085/events/ShowByStartDate
    @GetMapping("/ShowByStartDate")
    public List<Event> ShowEventOrderByStartDate(){
        return eventService.ShowEventOrderByStartDate();
    }
    //http://localhost:8085/events/UpdateEvent
    @PutMapping("/UpdateEvent")
    public String UpdateEvent(Event event, List<MultipartFile> files) throws IOException{
        return eventService.updateEvent(event,files);
    }
    //http://localhost:8085/events/DeleteEvent/{eventId}
    @DeleteMapping("/DeleteEvent/{eventId}")
    public void DeleteEvent(@PathVariable("eventId") Integer eventId) {
        eventService.DeleteEvent(eventId);
    }
    }

