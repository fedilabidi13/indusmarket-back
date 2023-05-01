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
@CrossOrigin(origins = "*")
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
    @GetMapping("/getEvent/{eventId}")
    public Event GetEvent(@PathVariable("eventId")Integer eventId){
        return eventService.getWithId(eventId);
    }
    //http://localhost:8085/events/ShowEventbyUser
    @GetMapping("/ShowEventbyUser")
    public List<Event> ShowEventbyUser(){
        return eventService.ShowEventbyUser();
    }

    //http://localhost:8085/events/addEvent
    @PostMapping("/addEvent")
    public Event AddEventWithPictureAndAssignToUser(@ModelAttribute Event event, @RequestParam("files")  List<MultipartFile> files) throws IOException{
        return eventService.AddEventWithPictureAndAssignToUser(event.getAdresse(),event,files);
    }

    //http://localhost:8085/events/showByStartDate
    @GetMapping("/showByStartDate")
    public List<Event> ShowEventOrderByStartDate(){
        return eventService.ShowEventOrderByStartDate();
    }

    //http://localhost:8085/events/updateEvent
    @PutMapping("/updateEvent")
    public void UpdateEvent(@ModelAttribute Event event, @RequestParam(name = "files",required=false) List<MultipartFile> files) throws IOException{
         eventService.updateEvent(event,files);
    }
    //http://localhost:8085/events/deleteEvent/{eventId}
    @DeleteMapping("/deleteEvent/{eventId}")
    public void DeleteEvent(@PathVariable("eventId") Integer eventId) {
        eventService.DeleteEvent(eventId);
    }
    //http://localhost:8085/events/accepEvent/{eventId}

    @PutMapping("accepEvent/{eventId}")
    public void AcceptEvent(@PathVariable("eventId") Integer eventId){
        eventService.AcceptEvent(eventId);
    }
    @DeleteMapping("/modDeleteEvent/{eventId}")
    public void deleteEvent(@PathVariable("eventId") Integer eventId){
        eventService.ModDeleteEvent(eventId);
    }
}

