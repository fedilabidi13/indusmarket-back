package tn.esprit.usermanagement.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.servicesImpl.ProfileService;

import java.io.IOException;

@RestController
@RequestMapping("/profile")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ProfileController {
    private ProfileService profileService;
    @PostMapping("/picture/update")
    public ResponseEntity<?> updatePicture(@RequestParam MultipartFile file) throws IOException {
        return ResponseEntity.ok(profileService.updatePicture(file));
    }
    @PostMapping("/address/update")
    public ResponseEntity<?> updateAdress(@RequestParam String address) throws IOException {
        return ResponseEntity.ok(profileService.updateAddress(address));
    }
}
