package tn.esprit.usermanagement.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.dto.AuthenticationRequest;
import tn.esprit.usermanagement.dto.AuthenticationResponse;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;

@Controller
@AllArgsConstructor
public class ModController {
    private UserRepo userRepo;
    private AuthenticationService authenticationService;
    private PasswordEncoder passwordEncoder;


    @GetMapping("/api/v1/auth/mod")
    public String showForm(Model model) {
        model.addAttribute("formData", new AuthenticationRequest("",""));
        return "form";
    }

    @PostMapping("/api/v1/auth/mod/submit")
    public String submitForm(@ModelAttribute("formData") AuthenticationRequest formData, Model model) {
        String jwt;
        // handle form submission

        String password = formData.getPassword();
        String email = formData.getEmail();
        // do something with the data
        if (userRepo.findByEmail2(formData.getEmail()).getFirtAttempt())
        {
            return "verif";
        }
        else
        {
            String response = authenticationService.authenticate(formData);
            //return authenticationService.authenticate(request).toString();
           // jwt = response.getJwtToken();
            model.addAttribute("jwt",response);
            return "ok";
        }
    }

    @PostMapping("/api/v1/auth/mod/confirmPassword")
    public String passForm(@ModelAttribute("formData") AuthenticationRequest formData2) {
        String password = formData2.getPassword();
       //String password = formData.getPassword();
        String email = formData2.getEmail();
        User user = userRepo.findByEmail2(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirtAttempt(false);
        userRepo.save(user);
        return "ok2";
    }


}
