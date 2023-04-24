package tn.esprit.usermanagement.controllers;

import com.maxmind.geoip2.exception.GeoIp2Exception;
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
@RequestMapping("/auth")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserRepo userRepo;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) throws IOException, GeoIp2Exception {
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
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) throws IOException, GeoIp2Exception {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping( "/confirm")
    public ResponseEntity<?> confirm(@RequestParam("token") String token) {
        return ResponseEntity.ok(authenticationService.confirmEmailToken(token));
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
    public String login2fa(@RequestParam String mail,@RequestParam String phone)
    {
        return authenticationService.login2fa(mail,phone);
    }
    @PostMapping("/resetPassword")
    public String requestPass(@RequestParam String email)
    {
        return authenticationService.requestResetPassword(email);
    }
    @PostMapping("/confirmPassword")
    public String confirmPass (@RequestParam String email,
                                          @RequestParam String phone,
                                          @RequestParam String pass)
    {
        return authenticationService.passwordResetConfirm(email, phone, pass);
    }
    @PostMapping("/confirmAddress")
    public String confirmAddress(@RequestParam String mail,
                                            @RequestParam String phone) throws IOException, GeoIp2Exception {
        return authenticationService.verifyLocation(mail,phone);
    }
    @PostMapping("/newPassword")
    public String loginModerator(@RequestParam String code,
                                            @RequestParam String pwd)
    {
        return authenticationService.loginMod(code,pwd);
    }
    @GetMapping("/get")
    public User getbymail(@RequestParam String email)
    {
        return userRepo.findByEmail2(email);
    }
}
