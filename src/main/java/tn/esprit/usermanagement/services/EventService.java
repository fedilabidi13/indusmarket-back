package tn.esprit.usermanagement.services;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Claims;
import tn.esprit.usermanagement.entities.Event;

import java.io.IOException;
import java.util.List;

public interface EventService {
    public Event AddEventWithPictureAndAssignToUser(Integer userId,String address, Event event, List<MultipartFile> files) throws IOException;
    public void DeleteEndEvent();
    public List<Event> ShowEventOrderByStartDate();
    public String updateEvent(Event event, List<MultipartFile> files) throws IOException;
    public List<Event> ShowEvents();
    public List<Event> ShowEventbyUser(Integer UserId);

    }

