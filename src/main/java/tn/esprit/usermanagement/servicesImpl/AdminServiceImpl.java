package tn.esprit.usermanagement.servicesImpl;

import java.time.temporal.ChronoUnit;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.enumerations.BanType;
import tn.esprit.usermanagement.enumerations.Role;
import tn.esprit.usermanagement.repositories.ProductRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.AdminService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    private PasswordGenerator passwordGenerator;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationService authenticationService;
    private UserRepo userRepo;
    private ProductRepo productRepo;
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
        //String token = jwtService.generateJwtToken(mod);
        String result = " Mod account : "+mod.getEmail()+" | "+ password;
        return result;
    }

    @Override
    public String banUser(String email) {
        Optional<User> optuser = userRepo.findByEmail(email);
        User user = optuser.get();
        user.setEnabled(false);
        user.setBanType(BanType.PERMA);
        userRepo.save(user);
        authenticationService.revokeAllUserTokens(user);
        return "account with Email: "+user.getEmail() +" is perma banned! ";
    }

    @Override
    public List<User> getUsers(Role role) {
        return userRepo.findByRole(role);
    }

    @Override
    public String suspendUser(String email) {
        User user = userRepo.findByEmail(email).get();
        if (user == null)
        {
            return "user does not exist";
        }
        user.setEnabled(false);
        user.setBanType(BanType.SUSPENSION);
        user.setBannedAt(LocalDateTime.now());
        user.setBanNumber(user.getBanNumber()+1);
        userRepo.save(user);
        authenticationService.revokeAllUserTokens(user);
        return "user suspended for 7 days";
    }


    @Override
    public String unbanUser(String email) {
        User user = userRepo.findByEmail(email).get();
        if (user == null)
        {
            return "user does not exist";
        }
        user.setEnabled(true);
        user.setBanType(BanType.NONE);
        user.setBannedAt(null);
        userRepo.save(user);
        return "user with email: "+user.getEmail()+" is unbanned! ";
    }

    @Override
    @Scheduled(fixedRate = 100000) // runs every second
    public String automaticUnbanUser() {
        List<User> userList = userRepo.findAll();
        for (User user: userList)
        {
            if ((user.getEnabled()==false) &&(user.getBanType().equals(BanType.SUSPENSION)))
            {
                if(ChronoUnit.DAYS.between(user.getBannedAt(), LocalDateTime.now())>=7)
                {
                    user.setEnabled(true);
                    user.setBannedAt(null);
                    user.setBanType(BanType.NONE);
                    userRepo.save(user);
                }
            }
        }
        return "Checking if there is anybody to unban";
    }

    @Override
    public String banIpUser(String email) {
        User user = userRepo.findByEmail(email).get();
        if (user == null)
        {
            return "user does not exist";
        }
        user.setEnabled(false);
        user.setBanType(BanType.IP);
        user.setBannedAt(LocalDateTime.now());
        userRepo.save(user);
        return "account with email: "+user.getEmail()+" is locked due to location change. ";
    }

    @Override
    public String approveProduct(Integer id) {
        Product product = productRepo.getReferenceById(id);
        if (product==null)
        {
            return "product not found!";
        }
        product.setValidated(true);
        productRepo.save(product);
        return "product approved! ";
    }


}
