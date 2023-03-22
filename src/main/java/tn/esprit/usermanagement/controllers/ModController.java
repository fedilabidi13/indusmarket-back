package tn.esprit.usermanagement.controllers;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.dto.AuthenticationRequest;
import tn.esprit.usermanagement.dto.AuthenticationResponse;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.AdminService;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/mod")
public class ModController {
    private UserRepo userRepo;
    private AuthenticationService authenticationService;
    private PasswordEncoder passwordEncoder;
    private AdminService adminService;


   @GetMapping("/demo")
   public ResponseEntity<?> sayHeMod()
   {
       return ResponseEntity.ok("hello from mod endpoint");
   }

    @PostMapping("/approveProduct")
    public String approveProd(@RequestParam Integer id)
    {
        return adminService.approveProduct(id);
    }


}