package tn.esprit.usermanagement.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.dto.AuthenticationRequest;
import tn.esprit.usermanagement.dto.AuthenticationResponse;
import tn.esprit.usermanagement.dto.RegistrationRequest;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserRepo userRepo;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request)
    {
        if (authenticationService.register(request) == null)
        {
            return ResponseEntity.ok("email exists");
        }

        else
        {
            return ResponseEntity.ok("account created");
        }

    }
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request)
    {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return authenticationService.confirmEmailToken(token);
    }
    @GetMapping("/verifyPhone")
    public String confirmPhone(@RequestParam("token")String token)
    {
        return authenticationService.confirmPhoneToken(token);
    }
    @PostMapping("/enable2fa")
    public String enabletwofactorsauth()
    {
        return authenticationService.enable2FA();
    }
    @PostMapping("/authenticate2fa")
    public ResponseEntity<?> login2fa(@RequestParam String mail,@RequestParam String phone)
    {
        return ResponseEntity.ok(authenticationService.login2fa(mail,phone));
    }
}
