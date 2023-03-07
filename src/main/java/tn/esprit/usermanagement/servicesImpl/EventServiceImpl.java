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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Service
public class EventServiceImpl implements EventService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    EventRepo eventRepo;
    @Autowired
    PicturesRepo picturesRepo;
    @Autowired
    AddressService addressService;

    @Override
    public List<Event> ShowEvents() {
        return eventRepo.findAll();
    }

    @Override
    public List<Event> ShowEventbyUser(Integer UserId) {
        return eventRepo.findAllByUserId(UserId);
    }

    @Override
    public Event AddEventWithPictureAndAssignToUser(Integer userId,String address, Event event, List<MultipartFile> files) throws IOException {
        User user = userRepo.findById(userId).get();
        event.setUser(user);
        event.setAddress(addressService.AddAddress(address));
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
        long Nbrdays = ChronoUnit.DAYS.between(savedEvent.getStartDate().toLocalDate(), savedEvent.getEndDate().toLocalDate());
        savedEvent.setPrice(Nbrdays*200);
        return eventRepo.save(savedEvent);
    }
@Override
public String updateEvent(Event event, List<MultipartFile> files) throws IOException {
    // Find the event by ID
    Optional<Event> optionalEvent = eventRepo.findById(event.getId());
    if (!optionalEvent.isPresent()) {
        return "Event not found !";
    }
    Event existingEvent = optionalEvent.get();

    // Check if the current date is within 5 days of the start date
    LocalDateTime now = LocalDateTime.now();
    Duration duration = Duration.between(now, existingEvent.getStartDate());
    if (duration.toDays() <= 5) {
        return "There is no time to update the Event!";
    }

    // Set the new start date after or the same as the old start date
    LocalDateTime newStartDate = event.getStartDate();
    if (event.getStartDate() == null) {
        event.setStartDate(existingEvent.getStartDate());
    } else if (event.getStartDate().isBefore(existingEvent.getStartDate())) {
        return "New start date must be after or the same as the existing start date!";
    } else {
        event.setStartDate(event.getStartDate());
    }

    if (files == null || files.isEmpty()) {
        event.setPictures(existingEvent.getPictures());
    } else {
        List<Pictures> picturesList = new ArrayList<>();
        for (MultipartFile file : files) {
            Pictures picture = new Pictures();
            byte[] data = file.getBytes();
            if (data.length > 500) {
                data = Arrays.copyOfRange(data, 0, 500);
            }
            picture.setData(data);
            picture.setContentType(file.getContentType());
            picturesList.add(picture);
        }
        List<Pictures> existingPictures = existingEvent.getPictures();
        for (Pictures picture : existingPictures) {
            picturesRepo.delete(picture);
        }
        picturesRepo.saveAll(picturesList);
        event.setPictures(picturesList);
    }
    if (event.getTitle() == null) {
        event.setTitle(existingEvent.getTitle());
    }
    if (event.getDescription() == null) {
        event.setDescription(existingEvent.getDescription());
    }
    if (event.getEndDate() == null) {
        event.setEndDate(existingEvent.getEndDate());
    } else {
        LocalDateTime newEndDate = event.getEndDate();
        if (newEndDate.isBefore(event.getStartDate())) {
            return "End date should be after the start date of the event!";
        }
        event.setEndDate(newEndDate);
    }
    if (event.getAdresse()==null){
        event.setAdresse(existingEvent.getAdresse());
    }
    else {
        String NewAddresse = event.getAdresse();
        event.setAddress(addressService.AddAddress(NewAddresse));


    }

    event.setTickets(existingEvent.getTickets());
    event.setUser(existingEvent.getUser());
    event.setPrice(existingEvent.getPrice());
    event.setAddress(existingEvent.getAddress());

    eventRepo.save(event);

    return "Event updated successfully";
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
