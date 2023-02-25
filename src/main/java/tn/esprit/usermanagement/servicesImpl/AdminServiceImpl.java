package tn.esprit.usermanagement.servicesImpl;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.enumerations.Role;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.AdminService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    private PasswordGenerator passwordGenerator;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private UserRepo userRepo;
    @Override
    public String addMod(String email) {
        String password = passwordGenerator.generatePassword();
        User mod = new User();
        mod.setEmail(email);
        mod.setRole(Role.MOD);
        mod.setEnabled(true);
        mod.setPassword(passwordEncoder.encode(password));
        mod.setEnabled(true);
        userRepo.save(mod);
        String token = jwtService.generateJwtToken(mod);
        token += " | "+mod.getEmail()+" | "+ password;
        return token;
    }

    @Override
    public String banUser(String email) {
        Optional<User> optuser = userRepo.findByEmail(email);
        User user = optuser.get();
        user.setEnabled(false);
        userRepo.save(user);
        return "banned! ";
    }

    @Override
    public List<User> getUsers(Role role) {
        return userRepo.findByRole(role);
    }


}
