package tn.esprit.usermanagement.servicesImpl;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.usermanagement.dto.AuthenticationRequest;
import tn.esprit.usermanagement.dto.AuthenticationResponse;
import tn.esprit.usermanagement.dto.RegistrationRequest;
import tn.esprit.usermanagement.entities.*;
import tn.esprit.usermanagement.entities.ForumEntities.Media;
import tn.esprit.usermanagement.enumerations.BanType;
import tn.esprit.usermanagement.enumerations.Role;
import tn.esprit.usermanagement.enumerations.TokenType;
import tn.esprit.usermanagement.repositories.JwtTokenRepo;
import tn.esprit.usermanagement.repositories.MediaRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.EmailSender;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final MediaRepo mediaRepo;
    private final IpService ipService;
    private final TwilioService twilioService;
    private final PhoneTokenService phoneTokenService;
    private final ConfirmationTokenService tokenService;
    private final UserRepo userRepo;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenRepo jwtTokenRepo;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailSender emailSender;
    private final EmailValidator emailValidator;
    public String authenticate(AuthenticationRequest request) throws IOException, GeoIp2Exception {
        var user = userRepo.findByEmail(request.getEmail()).orElse(null);

        if (user == null )
        {
            return "there is no account associated with such email! ";
        }
        if ((user.getRole().equals(Role.MOD))&&(user.getFirtAttempt()==true))
        {
            String tokenMod = UUID.randomUUID().toString();


            ConfirmationToken confirmationToken = new ConfirmationToken(tokenMod, LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(1),
                    user);
            tokenService.saveConfirmationToken(confirmationToken);
            emailSender.send(request.getEmail(),buildEmailMod(tokenMod,user));
            return "First Attempt detected! An email is sent";

        }
        if ((user.getEnabled()==false)&&(user.getBanType().equals(BanType.LOCK)))
        {
            return "account is locked. Verification is needed! ";
        }
        if ((user.getEnabled()==false)&&(user.getBanType().equals(BanType.PERMA)))
        {
            return "account is perma Banned! ";
        }
        if ((user.getEnabled()==false)&&(user.getBanType().equals(BanType.SUSPENSION)))
        {
            return "account is suspended for 7 days! ";
        }
        if ((user.getEnabled()==false)&&(user.getBanType().equals(BanType.IP)))
        {
            return "account is locked due to location change. Verification is needed! ";
        }
        if (!(user.getCountry().equals(ipService.getCountry())))
        {
            log.error(ipService.getCountry());
            log.error(user.getCountry());
            user.setEnabled(false);
            user.setBanType(BanType.IP);
            userRepo.save(user);
            String token = UUID.randomUUID().toString();
            String phoneCode = twilioService.generateCode();
            PhoneToken phoneToken = new PhoneToken(phoneCode, LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(1),
                    user);
            ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(1),
                    user);
            tokenService.saveConfirmationToken(confirmationToken);
            phoneTokenService.saveConfirmationToken(phoneToken);
            String link = "http://localhost:8085/auth/confirm?token="+token;
            emailSender.send(request.getEmail(),buildEmailVerif(token, user));
            var jwtTokenString = jwtService.generateJwtToken(user);
            twilioService.sendCode(String.valueOf(user.getPhoneNumber()),phoneCode);

            return "new Location detetected plz verify your account!";
        }

        if (user.getTwoFactorsAuth()==true)
        {

            String token = UUID.randomUUID().toString();
            String phoneCode= twilioService.generateCode();
            PhoneToken phoneToken = new PhoneToken(phoneCode, LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(1),
                    user);
            ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(1),
                    user);
            tokenService.saveConfirmationToken(confirmationToken);
            phoneTokenService.saveConfirmationToken(phoneToken);
            emailSender.send(request.getEmail(),buildEmailVerif(token,user));
            return "2fa required. Email and phone verification codes were sent.";
        }




        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));


        var jwtTokenString = "";
        jwtTokenString=jwtService.generateJwtToken(user);
        revokeAllUserTokens(user);
        saveJwtToken(user, jwtTokenString);
        return jwtTokenString;
    }

    private void saveJwtToken(User user, String jwtTokenString) {
        var jwtToken = JwtToken.builder()
                .token(jwtTokenString)
                .user(user)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        jwtTokenRepo.save(jwtToken);
    }

    public User register(RegistrationRequest request) throws IOException, GeoIp2Exception {

        User user2 = userRepo.findByEmail2(request.getEmail());
        if (user2!= null)
        {
            return null;
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .banType(BanType.LOCK)
                .phoneNumber(request.getPhoneNumber())
                .enabled(false)
                .country(ipService.getCountry())
                .twoFactorsAuth(false)
                .banNumber(0)
                .build();
        Media media = new Media();
        media.setName("default image");
        //todo  put an appropriate image url
        media.setImagenUrl("http://localhost/default.png");
        mediaRepo.save(media);
        user.setPicture(media);
        userRepo.save(user);
        String token = UUID.randomUUID().toString();
        String phoneCode= twilioService.generateCode();
        PhoneToken phoneToken = new PhoneToken(phoneCode, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1),
                user);
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1),
                user);
        tokenService.saveConfirmationToken(confirmationToken);
        phoneTokenService.saveConfirmationToken(phoneToken);
        String link = "http://localhost:8085/auth/confirm?token="+token;
        emailSender.send(request.getEmail(),buildEmail2(user,link));
        var jwtTokenString = jwtService.generateJwtToken(user);
        twilioService.sendCode(String.valueOf(user.getPhoneNumber()),phoneCode);

        //saveJwtToken(user,jwtTokenString);
        return user;
    }
    @Transactional
    public String confirmPhoneToken(String token)
    {
        PhoneToken phoneToken = phoneTokenService
                .getToken(token)
                .orElse(null);
        if (phoneToken==null)
        {
            return "token not found";
        }

        if (phoneToken.getConfirmedAt() != null) {
            return "phone already confirmed";
        }

        LocalDateTime expiredAt = phoneToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            String phoneCode= twilioService.generateCode();
            PhoneToken confirmationToken2 = new PhoneToken(phoneCode, LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(1),
                    phoneToken.getUser());
            phoneTokenService.saveConfirmationToken(confirmationToken2);
            twilioService.sendCode(String.valueOf(phoneToken.getUser().getPhoneNumber()),phoneCode);
            return "phone token expired. A new one is sent!";
        }

        phoneTokenService.setConfirmedAt(token);

        User user = phoneToken.getUser();
        user.setPhoneNumberVerif(true);
        userRepo.save(user);

        return "Phone confirmed";
    }
    @Transactional
    public String confirmEmailToken(String token) {
        ConfirmationToken confirmationToken = tokenService
                .getToken(token).get();
        if (confirmationToken == null)
        {
            return "token not found";
        }

        if (confirmationToken.getConfirmedAt() != null) {
            return ("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            String token2 = UUID.randomUUID().toString();

            ConfirmationToken confirmationToken2 = new ConfirmationToken(token2, LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(1),
                    confirmationToken.getUser());
            tokenService.saveConfirmationToken(confirmationToken2);
            String link = "http://localhost:8085/auth/confirm?token="+token2;
            emailSender.send(confirmationToken.getUser().getEmail(),buildEmail2(confirmationToken.getUser(),link));
            return "email expired a new Email is sent!";
        }

        tokenService.setConfirmedAt(token);
        //userService.enableAppUser(
          //      confirmationToken.getUser().getEmail());
        User user = confirmationToken.getUser();
        user.setEmailVerif(true);
        user.setBanType(BanType.NONE);
        userRepo.save(user);
        userService.enableAppUser(confirmationToken.getUser().getEmail());
        return "Email confirmed ";
    }
    @Transactional
    public String login2fa(String mailToken,String phoneCode)
    {
        Boolean phoneConfirmed = false;
        Boolean emailConfirmed = true;
        ConfirmationToken confirmationToken = tokenService
                .getToken(mailToken).get();
        if (confirmationToken == null)
        {
            return "invalid email token";
        }



        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            String token2 = UUID.randomUUID().toString();

            ConfirmationToken confirmationToken2 = new ConfirmationToken(token2, LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(1),
                    confirmationToken.getUser());
            tokenService.saveConfirmationToken(confirmationToken2);
            //String link = "http://localhost:8085/auth/confirm?token="+token2;
            emailSender.send(confirmationToken.getUser().getEmail(),buildEmailVerif(token2, confirmationToken2.getUser() ));
            return "email expired a new Email is sent!";
        }


        tokenService.setConfirmedAt(mailToken);
        emailConfirmed= true;
        PhoneToken phoneToken = phoneTokenService
                .getToken(phoneCode)
                .orElse(null);
        if (phoneToken==null)
        {
            return "phone token not found";
        }



        LocalDateTime phoneexpiredAt = phoneToken.getExpiresAt();

        if (phoneexpiredAt.isBefore(LocalDateTime.now())) {
            String code= twilioService.generateCode();
            PhoneToken confirmationToken2 = new PhoneToken(code, LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(1),
                    phoneToken.getUser());
            phoneTokenService.saveConfirmationToken(confirmationToken2);
            twilioService.sendCode(String.valueOf(phoneToken.getUser().getPhoneNumber()),code);
            return "phone token expired. A new one is sent!";
        }

        phoneTokenService.setConfirmedAt(phoneCode);

        User user = phoneToken.getUser();

        var jwtTokenString = "";
        jwtTokenString=jwtService.generateJwtToken(user);
        revokeAllUserTokens(user);
        saveJwtToken(user, jwtTokenString);
        return jwtTokenString;

    }
    @Transactional
    public String loginMod(String mailToken,String newPassword)
    {

        ConfirmationToken confirmationToken = tokenService
                .getToken(mailToken).get();
        if (confirmationToken == null)
        {
            return "invalid email token";
        }



        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            String token2 = UUID.randomUUID().toString();

            ConfirmationToken confirmationToken2 = new ConfirmationToken(token2, LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(1),
                    confirmationToken.getUser());
            tokenService.saveConfirmationToken(confirmationToken2);
            emailSender.send(confirmationToken.getUser().getEmail(),buildEmailMod(token2, confirmationToken2.getUser() ));
            return "email expired a new Email is sent!";
        }


        tokenService.setConfirmedAt(mailToken);


        User user = confirmationToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setFirtAttempt(false);
        userRepo.save(user);


        return "password updated! U can login via the mod portal";

    }
    @Transactional
    public String verifyLocation(String mailToken,String phoneCode) throws IOException, GeoIp2Exception {
        ConfirmationToken confirmationToken = tokenService
                .getToken(mailToken).get();
        if (confirmationToken == null)
        {
            return "invalid email token";
        }



        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            String token2 = UUID.randomUUID().toString();

            ConfirmationToken confirmationToken2 = new ConfirmationToken(token2, LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(1),
                    confirmationToken.getUser());
            tokenService.saveConfirmationToken(confirmationToken2);
            //String link = "http://localhost:8085/auth/confirm?token="+token2;
            emailSender.send(confirmationToken.getUser().getEmail(),buildEmailVerif(token2, confirmationToken2.getUser()));
            return "email expired a new Email is sent!";
        }

        tokenService.setConfirmedAt(mailToken);
        PhoneToken phoneToken = phoneTokenService
                .getToken(phoneCode)
                .orElse(null);
        if (phoneToken==null)
        {
            return "phone token not found";
        }



        LocalDateTime phoneexpiredAt = phoneToken.getExpiresAt();

        if (phoneexpiredAt.isBefore(LocalDateTime.now())) {
            String code= twilioService.generateCode();
            PhoneToken confirmationToken2 = new PhoneToken(code, LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(1),
                    phoneToken.getUser());
            phoneTokenService.saveConfirmationToken(confirmationToken2);
            twilioService.sendCode(String.valueOf(phoneToken.getUser().getPhoneNumber()),code);
            return "phone token expired. A new one is sent!";
        }

        phoneTokenService.setConfirmedAt(phoneCode);

        User user = phoneToken.getUser();

        user.setEnabled(true);
        user.setBanType(BanType.NONE);
        user.setCountry(ipService.getCountry());
        userRepo.save(user);
        return "location approved u can login now! ";

    }
    public void revokeAllUserTokens(User user) {
        var validUserTokens = jwtTokenRepo.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        jwtTokenRepo.saveAll(validUserTokens);
    }

    @Transactional
    public String requestResetPassword(String email)
    {

        User user = userRepo.findByEmail(email).get();
        if (user == null)
        {
            return "user not found";
        }
        String token = UUID.randomUUID().toString();
        String phoneCode= twilioService.generateCode();
        PhoneToken phoneToken = new PhoneToken(phoneCode, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1),
                user);
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1),
                user);
        tokenService.saveConfirmationToken(confirmationToken);
        phoneTokenService.saveConfirmationToken(phoneToken);
        String link = "http://localhost:8085/auth/confirm?token="+token;
        emailSender.send(user.getEmail(),buildEmailVerif(token, confirmationToken.getUser()));
        return "verification required. Email and phone verification codes were sent.";
    }
    @Transactional
    public String passwordResetConfirm(String mailToken,String phoneCode,String password)
    {
        ConfirmationToken confirmationToken = tokenService
                .getToken(mailToken).get();
        if (confirmationToken == null)
        {
            return "invalid email token";
        }



        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            String token2 = UUID.randomUUID().toString();

            ConfirmationToken confirmationToken2 = new ConfirmationToken(token2, LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(1),
                    confirmationToken.getUser());
            tokenService.saveConfirmationToken(confirmationToken2);
            //String link = "http://localhost:8085/auth/confirm?token="+token2;
            emailSender.send(confirmationToken.getUser().getEmail(),token2);
            return "email expired a new Email is sent!";
        }

        tokenService.setConfirmedAt(mailToken);

        PhoneToken phoneToken = phoneTokenService
                .getToken(phoneCode)
                .orElse(null);
        if (phoneToken==null)
        {
            return "phone token not found";
        }



        LocalDateTime phoneexpiredAt = phoneToken.getExpiresAt();

        if (phoneexpiredAt.isBefore(LocalDateTime.now())) {
            String code= twilioService.generateCode();
            PhoneToken confirmationToken2 = new PhoneToken(code, LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(1),
                    phoneToken.getUser());
            phoneTokenService.saveConfirmationToken(confirmationToken2);
            twilioService.sendCode(String.valueOf(phoneToken.getUser().getPhoneNumber()),code);
            return "phone token expired. A new one is sent!";
        }

        phoneTokenService.setConfirmedAt(phoneCode);

        User user = phoneToken.getUser();
        user.setPassword(passwordEncoder.encode(password));
        userRepo.save(user);
        return "password updated";
    }
    private String buildEmail2(User user,String link)
    {
        return "<!DOCTYPE html>\n" +
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
                "        <h1 style=\"color : #3CAEA3;\">Hi "+user.getFirstName()+" "+user.getLastName()+"</h1>\n" +
                "        <h3>Thank you for registering to indusmarket. Please click on the below link to activate your account:</h3>\n" +
                "<p style=\"color : red\">Link will expire in 15 minutes.</p>\n" +
                "        <p><a href="+link+" class=\"button\">Verification Link</a></p>\n" +
                "\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>\n";
    }

    private String buildEmailVerif(String token,User user)
    {
        return "<!DOCTYPE html>\n" +
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
                "        <h1 style=\"color : #3CAEA3;\">Hi "+user.getFirstName()+" "+user.getLastName()+"</h1>\n" +
                "        <p>We noticed there is a problem connecting to your account</p>\n" +
                "        <p>this is your email verification code:</p>\n" +
                "\n" +
                "<p style=\"color : red\">"+token+"</p>\n" +
                "       \n" +
                "\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>\n";
    }
    private String buildEmailMod(String token,User user)
    {
        return "<!DOCTYPE html>\n" +
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
                "        <p>this is your email verification code:</p>\n" +
                "\n" +
                "<p style=\"color : red\">"+token+"</p>\n" +
                "       \n" +
                "\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>\n";
    }
    private String buildEmail(String name, String link) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Titre de l'email</title>\n" +
                "    <style>\n" +
                "      /* Styles pour l'arrière-plan uni */\n" +
                "      body {\n" +
                "        background-color: #F5F5F5;\n" +
                "        margin: 0;\n" +
                "        padding: 0;\n" +
                "\tfont-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif;\n" +
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
                "        <h1 color=\"#3CAEA3\">Hi +user.getFirstName()+\" \"+user.getLastName()</h1>\n" +
                "        <p>Thank you for registering to indusmarket. Please click on the below link to activate your account:</p>\n" +
                "<p color=\"red\">Link will expire in 15 minutes.</p>\n" +
                "        <p><a href=\"link\" class=\"button\">Verification Link</a></p>\n" +
                "\n" +
                "      </div>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>\n";
    }
    public User currentlyAuthenticatedUser()
    {
        //todo improve function
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email).get();
    }
    public String enable2FA()
    {
        User user = currentlyAuthenticatedUser();
        user.setTwoFactorsAuth(true);
        userRepo.save(user);
        return "Two Factors Auth enabled! ";
    }
    // todo reset password
}
