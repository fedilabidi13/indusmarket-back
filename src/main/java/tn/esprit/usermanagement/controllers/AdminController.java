package tn.esprit.usermanagement.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.enumerations.Role;
import tn.esprit.usermanagement.services.AdminService;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/admin")
@AllArgsConstructor
public class AdminController {
    private AdminService adminService;
    private AuthenticationService authenticationService;
    @GetMapping("/addMod")
    public String createModAccount(@RequestBody String email)
    {
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
    //todo convert string to Enum type role
        Role enumrole = Role.valueOf(role);
        return adminService.getUsers(enumrole);
    }

    @GetMapping("/currentUser")
    public User getcurrentuser()
    {
        return authenticationService.currentlyAuthenticatedUser();
    }
}
