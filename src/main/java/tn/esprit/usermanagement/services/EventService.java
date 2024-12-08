package tn.esprit.usermanagement.services;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Claims;
import tn.esprit.usermanagement.entities.Event;

import java.io.IOException;
import java.util.List;

public interface EventService {
    public Event AddEventWithPictureAndAssignToUser(String address, Event event, List<MultipartFile> files) throws IOException;
    public List<Event> ShowEventOrderByStartDate();
    public String updateEvent(Event event, List<MultipartFile> files) throws IOException;
    public List<Event> ShowEvents();
  public List<Event> ShowEventbyUser();
    public void DeleteEvent(Integer eventId);
    public void AcceptEvent(Integer eventId);
    public void ModDeleteEvent(Integer eventId);
    public void deletePassedEvent();
    public Event getWithId(Integer eventId);
    }

