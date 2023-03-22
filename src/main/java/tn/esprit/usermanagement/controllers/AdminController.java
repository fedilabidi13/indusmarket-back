package tn.esprit.usermanagement.controllers;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.enumerations.Role;
import tn.esprit.usermanagement.services.AdminService;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor

public class AdminController {
    private AdminService adminService;
    private AuthenticationService authenticationService;
    @GetMapping("/demo")
    public ResponseEntity<?> sayHelloAdmin()
    {
        return ResponseEntity.ok("hello from admin endpoint");
    }
    @GetMapping("/addMod")
    public String createModAccount(@RequestBody String email) throws IOException, GeoIp2Exception {
        return adminService.addMod(email);
    }
    @GetMapping("/ban")
    public String banUser(@RequestBody String email)
    {
        return adminService.banUser(email);
    }
    @GetMapping("/findByRole")
    public List<User> getuserss(@RequestParam String role)
    {
        Role enumrole = Role.valueOf(role);
        return adminService.getUsers(enumrole);
    }

    @GetMapping("/currentUser")
    public ResponseEntity<?> getcurrentuser()
    {
        return ResponseEntity.ok(authenticationService.currentlyAuthenticatedUser().getEmail());
    }
}
