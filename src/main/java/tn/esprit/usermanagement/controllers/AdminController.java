package tn.esprit.usermanagement.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.usermanagement.services.AdminService;

@RestController
@RequestMapping("/api/v1/auth/admin")
@AllArgsConstructor
public class AdminController {
    private AdminService adminService;
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
}
