package tn.esprit.usermanagement.servicesImpl;

import com.cloudinary.Cloudinary;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Address;
import tn.esprit.usermanagement.entities.ForumEntities.Media;
import tn.esprit.usermanagement.repositories.AddressRepo;
import tn.esprit.usermanagement.repositories.MediaRepo;
import tn.esprit.usermanagement.repositories.UserRepo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class ProfileService {
    private AuthenticationService authenticationService;
    private Cloudinary cloudinary;
    private MediaRepo mediaRepo;
    private UserRepo userRepo;
    private AddressService addressService;
    private AddressRepo addressRepo;
    public String updatePicture(MultipartFile file) throws IOException {

        Media media = new Media();
        String url = cloudinary.uploader()
                .upload(file.getBytes(),
                        Map.of("public_id", UUID.randomUUID().toString()))
                .get("url")
                .toString();
        media.setImagenUrl(url);
        media.setName(file.getName());
        mediaRepo.save(media);
        authenticationService.currentlyAuthenticatedUser().setPicture(media);
        userRepo.save(authenticationService.currentlyAuthenticatedUser());
        return "Image Updated! ";
    }
    public String updateAddress(String adresse) throws IOException {
        Address address = addressService.AddAddress(adresse);
        addressRepo.save(address);
        authenticationService.currentlyAuthenticatedUser().setAddress(address);
        userRepo.save(authenticationService.currentlyAuthenticatedUser());
        return "address updated!";

    }
}
