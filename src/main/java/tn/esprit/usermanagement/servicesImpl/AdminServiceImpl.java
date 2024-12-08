package tn.esprit.usermanagement.servicesImpl;

import java.io.IOException;
import java.time.temporal.ChronoUnit;

import com.maxmind.geoip2.exception.GeoIp2Exception;
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
    private IpService ipService;
    private AuthenticationService authenticationService;
    private UserRepo userRepo;
    private ProductRepo productRepo;
    private EmailService emailService;
    @Override
    public String addMod(String email) throws IOException, GeoIp2Exception {
        String password = passwordGenerator.generatePassword();
        User mod = new User();
        mod.setEmail(email);
        mod.setRole(Role.MOD);
        mod.setTwoFactorsAuth(false);
        mod.setFirtAttempt(true);
        mod.setCountry(ipService.getCountry());
        mod.setPassword(passwordEncoder.encode(password));
        mod.setEnabled(true);
        mod.setBanType(BanType.NONE);
        userRepo.save(mod);
        String mail = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Titre de l'email</title>\n" +
                "    <style>\n" +
                "      /* Styles pour l'arrière-plan uni */\n" +
                "      body {\n" +
                "        background-color: #F5F5F5;\n" +
                "        margin: 0;\n" +
                "        padding: 0;    \n" +
                "\tfont-family: Arial, sans-serif;\n" +
                "\n" +
                "      }\n" +
                "      /* Styles pour le conteneur principal */\n" +
                "      .container {\n" +
                "        max-width: 600px;\n" +
                "        margin: 0 auto;\n" +
                "        background-color: #FFFFFF;\n" +
                "        padding: 20px;\n" +
                "        height: 100vh;\n" +
                "        display: flex;\n" +
                "        flex-direction: column;\n" +
                "        justify-content: center;\n" +
                "      }\n" +
                "      /* Styles pour le logo de l'entreprise */\n" +
                "      .logo {\n" +
                "        display: block;\n" +
                "        margin: -20px auto 20px;\n" +
                "        width: 100px;\n" +
                "        height: auto;\n" +
                "      }\n" +
                "      /* Styles pour le corps du texte */\n" +
                "      .text {\n" +
                "        text-align: center;\n" +
                "      }\n" +
                "      /* Styles pour le bouton animé */\n" +
                "      .button {\n" +
                "        display: inline-block;\n" +
                "        font-size: 16px;\n" +
                "        font-weight: bold;\n" +
                "        color: #3CAEA3;\n" +
                "        background-color: transparent;\n" +
                "        border-radius: 5px;\n" +
                "        padding: 10px 20px;\n" +
                "        border: 2px solid #3CAEA3;\n" +
                "        text-decoration: none;\n" +
                "        transition: all 0.5s ease;\n" +
                "      }\n" +
                "      .button:hover {\n" +
                "        background-color: #3CAEA3;\n" +
                "        color: #FFFFFF;\n" +
                "      }\n" +
                "    </style>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div class=\"container\">\n" +
                "      <img src=\"https://i.ibb.co/nkrBqck/334886508-513260607680644-3515218608247778867-n.png\" alt=\"indusmarket logo\" padding-left=\"60%\" height=\"70px\" width=\"130px\">\n" +
                "<br>     \n" +
                " <div class=\"text\">\n" +
                "        <h1 style=\"color : #3CAEA3;\">Dear moderator</h1>\n" +
                "        <p>Since this the first login Attempt on your account</p>\n" +
                "        <p>this is your password:</p>\n" +
                "\n" +
                "<p style=\"color : red\">"+password+"</p>\n" +
                "       \n" +
                "\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>\n";
        emailService.send(mod.getEmail(), mail);
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
