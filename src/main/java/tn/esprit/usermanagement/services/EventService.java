package tn.esprit.usermanagement.services;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Event;

import java.io.IOException;
import java.util.List;

public interface EventService {
    public Event AddEventWithPictureAndAssignToUser(Integer userId, Event event, List<MultipartFile> files) throws IOException;
    public void DeleteEndEvent();
    public List<Event> ShowEventOrderByStartDate();
}

