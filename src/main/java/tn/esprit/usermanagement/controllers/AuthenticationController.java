package tn.esprit.usermanagement.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.AuthenticationRequest;
import tn.esprit.usermanagement.entities.AuthenticationResponse;
import tn.esprit.usermanagement.entities.RegistrationRequest;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserRepo userRepo;

    @PostMapping("/register")
    public User register(@RequestBody RegistrationRequest request)
    {
        return authenticationService.register(request);

    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request)
    {

        return ResponseEntity.ok(authenticationService.authenticate(request));

    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return authenticationService.confirmToken(token);
    }

}
