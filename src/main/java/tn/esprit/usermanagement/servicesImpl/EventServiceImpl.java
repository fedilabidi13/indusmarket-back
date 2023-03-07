package tn.esprit.usermanagement.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Event;
import tn.esprit.usermanagement.entities.Pictures;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.EventRepo;
import tn.esprit.usermanagement.repositories.PicturesRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.EventService;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class EventServiceImpl implements EventService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    EventRepo eventRepo;
    @Autowired
    PicturesRepo picturesRepo;


    @Override
    public Event AddEventWithPictureAndAssignToUser(Integer userId, Event event, List<MultipartFile> files) throws IOException {
        User user = userRepo.findById(userId).get();
        event.setUser(user);
        Event savedEvent = eventRepo.save(event);
        List<Pictures> picturesList = new ArrayList<>();
        for (MultipartFile file : files) {
            Pictures picture = new Pictures();
            picture.setContentType(file.getContentType());
            byte[] data = file.getBytes();
            if (data.length > 500) { // check if the file is too large
                data = Arrays.copyOfRange(data, 0, 500); // truncate the data
            }
            picture.setData(data);
            picturesList.add(picture);
        }
        picturesRepo.saveAll(picturesList);
        savedEvent.setPictures(picturesList);
        return eventRepo.save(savedEvent);
    }



    @Scheduled(fixedRate = 10000) // runs every second
    @Override
    public void DeleteEndEvent() {
        LocalDateTime limite = LocalDateTime.now().minusMonths(12);
        List<Event> Events = eventRepo.findByEndDateIsBefore(limite);
        for (Event event : Events) {
            eventRepo.delete(event);
        }
    }

    @Override
    public List<Event> ShowEventOrderByStartDate() {
        return eventRepo.OrderByStartDate();


    }
}
