package tn.esprit.usermanagement.controllers;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.dto.AuthenticationRequest;
import tn.esprit.usermanagement.dto.AuthenticationResponse;
import tn.esprit.usermanagement.entities.Claims;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.enumerations.StatusClaims;
import tn.esprit.usermanagement.enumerations.TypeClaim;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.AdminService;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;
import tn.esprit.usermanagement.servicesImpl.ClaimsServiceImpl;

import java.io.IOException;
import java.util.List;

@Controller
@AllArgsConstructor
public class ModController {
    private UserRepo userRepo;
    private AuthenticationService authenticationService;
    private PasswordEncoder passwordEncoder;
    private AdminService adminService;
   private ClaimsServiceImpl claimsService;

    @GetMapping("/mod")
    public String showForm(Model model) {
        model.addAttribute("formData", new AuthenticationRequest("",""));
        return "form";
    }
    @PostMapping("/mod/submit")
    public String submitForm(@ModelAttribute("formData") AuthenticationRequest formData, Model model) throws IOException, GeoIp2Exception {
        if (userRepo.findByEmail2(formData.getEmail()).getFirtAttempt())
        {
            return "verif";
        }
        else
        {
            String response = authenticationService.authenticate(formData);
            model.addAttribute("jwt",response);
            return "ok";
        }
    }
    @PostMapping("/mod/confirmPassword")
    public String passForm(@ModelAttribute("formData") AuthenticationRequest formData2) {
        String password = formData2.getPassword();
        String email = formData2.getEmail();
        User user = userRepo.findByEmail2(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirtAttempt(false);
        userRepo.save(user);
        return "ok2";
    }
    @PostMapping("/mod/approveProduct")
    public String approveProd(@RequestParam Integer id)
    {
        return adminService.approveProduct(id);
    }

}
