package tn.esprit.usermanagement.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.servicesImpl.ProfileService;

import java.io.IOException;

@RestController
@RequestMapping("/profile")
@AllArgsConstructor
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
