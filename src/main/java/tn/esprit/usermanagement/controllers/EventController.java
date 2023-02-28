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
public class EventController {
@Autowired
EventServiceImpl eventService;


@PostMapping("/AddEventWithPictureAndAssignToUser/{userId}")
public Event AddEventWithPictureAndAssignToUser(@PathVariable("userId") Integer userId, @ModelAttribute Event event, @RequestParam("files") List<MultipartFile> files) throws IOException{
    return eventService.AddEventWithPictureAndAssignToUser(userId,event,files);
}
@GetMapping("/ShowByStartDate")
public List<Event> ShowEventOrderByStartDate(){
    return eventService.ShowEventOrderByStartDate();

}


    /*@GetMapping("/files/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable Integer id) throws IOException {
        Pictures fileEntity = picturesService.getPictureById(id).orElse(null);
        String filetype = fileEntity.getContentType().substring(5);
        if (fileEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Resource resource = new ByteArrayResource(fileEntity.getData());

        // Detect the content type of the file using Tika
        Tika tika = new Tika();
        // String contentType = tika.detect(fileEntity.getData());
        String contentType = fileEntity.getContentType();

        // If the content type cannot be detected, fall back to the content type stored in the database
        if (contentType == null || contentType.equals("application/octet-stream")) {
            contentType = fileEntity.getContentType();
        }

        if (contentType == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Content type not detected or set in database.");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }*/
}

