package tn.esprit.usermanagement.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Event;
import tn.esprit.usermanagement.entities.ForumEntities.Pictures;
import tn.esprit.usermanagement.entities.Ticket;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.EventRepo;
import tn.esprit.usermanagement.repositories.PicturesRepo;
import tn.esprit.usermanagement.repositories.TicketRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.EventService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    @Autowired
    AddressService addressService;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    TicketRepo ticketRepo;
    @Autowired
    EmailService emailService;
    @Override
    public List<Event> ShowEvents() {
        return eventRepo.findAll();
    }

    @Override
    public List<Event> ShowEventbyUser() {
        return eventRepo.findAllByUserId(authenticationService.currentlyAuthenticatedUser().getId());
    }

    @Override
    public void DeleteEvent(Integer eventId) {
        if (eventRepo.findById(eventId).get().getTickets().isEmpty()){
            eventRepo.delete(eventRepo.findById(eventId).get());
        }
    }

    @Override
    public void AcceptEvent(Integer eventId) {
        eventRepo.findById(eventId).get().setAccepted(true);
    }

    @Override
    public void ModDeleteEvent(Integer eventId) {
        if (!eventRepo.findById(eventId).get().getTickets().isEmpty()){
            for (Ticket ticket : eventRepo.findById(eventId).get().getTickets()){
                emailService.sendEventEmail(ticket.getUser().getEmail(),"The event : "+ eventRepo.findById(eventId).get().getTitle() + "was deleted.");
                ticketRepo.delete(ticket);
            }
            eventRepo.delete(eventRepo.findById(eventId).get());
        }
        eventRepo.delete(eventRepo.findById(eventId).get());
    }
    @Override
    public Event AddEventWithPictureAndAssignToUser(String address, Event event, List<MultipartFile> files) throws IOException {
        event.setUser(authenticationService.currentlyAuthenticatedUser());
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
        double Nbrdays = ChronoUnit.DAYS.between(savedEvent.getStartDate().toLocalDate(), savedEvent.getEndDate().toLocalDate());
        savedEvent.setPrice(Nbrdays*200);
        return eventRepo.save(savedEvent);
    }
@Override
public String updateEvent(Event event, List<MultipartFile> files) throws IOException {
    // Find the event by ID
    Event optionalEvent = eventRepo.findById(event.getId()).get();
    if (optionalEvent.getTickets().isEmpty()) {
        event.setStartDate(optionalEvent.getStartDate());
        event.setEndDate(optionalEvent.getEndDate());
        if (files == null || files.isEmpty()) {
            event.setPictures(optionalEvent.getPictures());
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
            List<Pictures> existingPictures = optionalEvent.getPictures();
            for (Pictures picture : existingPictures) {
                picturesRepo.delete(picture);
            }
            picturesRepo.saveAll(picturesList);
            event.setPictures(picturesList);
        }
        String NewAddresse = event.getAdresse();
        event.setAddress(addressService.AddAddress(NewAddresse));
        event.setUser(optionalEvent.getUser());
        eventRepo.save(event);
        return "Event updated successfully";
    }
    return "you cant update this event now";
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
