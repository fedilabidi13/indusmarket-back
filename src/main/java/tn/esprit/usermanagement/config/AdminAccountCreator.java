package tn.esprit.usermanagement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.enumerations.BanType;
import tn.esprit.usermanagement.enumerations.Role;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.servicesImpl.IpService;

public class AdminAccountCreator  {
/*
    private UserRepo userRepository;
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IpService ipService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // check if admin account already exists
        if (userRepository.findByEmail2("admin") != null) {
            return;
        }

        // create new admin account
        User admin = new User();
        admin.setEmail("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setRole(Role.ADMIN);
        admin.setBanType(BanType.NONE);
        admin.setEnabled(true);
        admin.setCountry(ipService.getCountry());
        userRepository.save(admin);
    }

 */
}