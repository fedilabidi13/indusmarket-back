package tn.esprit.usermanagement.servicesImpl;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.*;
import tn.esprit.usermanagement.entities.ForumEntities.Media;
import tn.esprit.usermanagement.repositories.*;
import tn.esprit.usermanagement.services.EventService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
    @Autowired
    Cloudinary cloudinary;
    @Autowired
    MediaRepo mediaRepo;
    @Autowired
    AddressRepo addressRepo;
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
        Event event = eventRepo.findById(eventId).get();
        if (event.getTickets().isEmpty()){
            eventRepo.delete(event);
        }
    }

    @Override
    public void AcceptEvent(Integer eventId) {
        Event event = eventRepo.findById(eventId).get();
        event.setAccepted(true);
        eventRepo.save(event);
    }

    @Override
    public void ModDeleteEvent(Integer eventId) {
        Event event = eventRepo.findById(eventId).get();
            for (Ticket ticket : event.getTickets()){
                emailService.sendEventEmail(ticket.getUser().getEmail(),"The event : "+ eventRepo.findById(eventId).get().getTitle() + "was deleted.");
                ticketRepo.delete(ticket);
        }
        eventRepo.delete(event);

    }



    @Override
    public Event AddEventWithPictureAndAssignToUser(String address, Event event, List<MultipartFile> files) throws IOException {
        event.setUser(authenticationService.currentlyAuthenticatedUser());
        event.setAddress(addressRepo.save(addressService.AddAddress(event.getAdresse())));
        Event savedEvent = eventRepo.save(event);
        List<Media> mediaList = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            Media media = new Media();
            String url = cloudinary.uploader()
                    .upload(multipartFile.getBytes(),
                            Map.of("public_id", UUID.randomUUID().toString()))
                    .get("url")
                    .toString();
            media.setImagenUrl(url);
            media.setName(multipartFile.getName());
            mediaList.add(media);
        }
        mediaRepo.saveAll(mediaList);
        savedEvent.setMedias(mediaList);
        double Nbrdays = ChronoUnit.DAYS.between(savedEvent.getStartDate().toLocalDate(), savedEvent.getEndDate().toLocalDate());
        savedEvent.setPrice(Nbrdays*200);
        return eventRepo.save(savedEvent);
    }
    @Override
    public String updateEvent(Event event, List<MultipartFile> files) throws IOException {
        // Find the event by ID
        Event optionalEvent = eventRepo.findById(event.getId()).get();
        Address newAdresse = addressService.AddAddress(event.getAdresse());
        addressRepo.save(newAdresse);
        event.setAddress(newAdresse);
        if (optionalEvent.getTickets().isEmpty()) {
            if (files == null || files.isEmpty()) {
                event.setMedias(optionalEvent.getMedias());
            } else {
                List<Media> mediaList = new ArrayList<>();
                for (MultipartFile multipartFile : files) {
                    Media media = new Media();
                    String url = cloudinary.uploader()
                            .upload(multipartFile.getBytes(),
                                    Map.of("public_id", UUID.randomUUID().toString()))
                            .get("url")
                            .toString();
                    media.setImagenUrl(url);
                    media.setName(multipartFile.getName());
                    mediaList.add(media);
                }
                List<Media> existingPictures = optionalEvent.getMedias();
                for (Media picture : existingPictures) {
                    mediaRepo.delete(picture);
                }
                mediaRepo.saveAll(mediaList);
                event.setMedias(mediaList);
            }
            event.setUser(optionalEvent.getUser());
            double Nbrdays = ChronoUnit.DAYS.between(event.getStartDate().toLocalDate(), event.getEndDate().toLocalDate());
            event.setPrice(Nbrdays*200);
            eventRepo.save(event);
            return "Event updated successfully";
        }
        return "you cant update this event now";
    }

    @Override
    public List<Event> ShowEventOrderByStartDate() {
        return eventRepo.OrderByStartDate();
    }
    @Scheduled(fixedRate = 10000) // runs every second
    @Override
    public void deletePassedEvent() {
    List<Event> events = eventRepo.findByEndDateIsBefore(LocalDateTime.now().minusDays(0));
        for (Event event : events){
        eventRepo.delete(event);
        }
    }

    @Override
    public Event getWithId(Integer eventId) {
        return eventRepo.findById(eventId).get();
    }
}
