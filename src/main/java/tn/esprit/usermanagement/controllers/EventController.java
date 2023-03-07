package tn.esprit.usermanagement.controllers;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.usermanagement.entities.Event;
import tn.esprit.usermanagement.servicesImpl.EventServiceImpl;
import tn.esprit.usermanagement.servicesImpl.PicturesServiceImpl;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventController {
@Autowired
EventServiceImpl eventService;

@GetMapping("/showEvents")
public List<Event> ShowEvents(){
    return eventService.ShowEvents();
}
    @GetMapping("/showEvents/{userId}")
    public List<Event> ShowEvents(@PathVariable("userId") Integer userId){
        return eventService.ShowEventbyUser(userId);
    }
@PostMapping("/AddEventWithPictureAndAssignToUser/{userId}")
public Event AddEventWithPictureAndAssignToUser(@PathVariable("userId") Integer userId, @ModelAttribute Event event, @RequestParam("files") List<MultipartFile> files) throws IOException{
    return eventService.AddEventWithPictureAndAssignToUser(userId,event.getAdresse(),event,files);
}
@GetMapping("/ShowByStartDate")
public List<Event> ShowEventOrderByStartDate(){
    return eventService.ShowEventOrderByStartDate();

}
@PutMapping("/UpdateEvent")
public String UpdateEvent(Event event, List<MultipartFile> files) throws IOException{
    return eventService.updateEvent(event,files);
    }

}

