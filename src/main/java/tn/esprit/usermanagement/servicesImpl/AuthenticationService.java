package tn.esprit.usermanagement.servicesImpl;

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
import tn.esprit.usermanagement.enumerations.BanType;
import tn.esprit.usermanagement.enumerations.Role;
import tn.esprit.usermanagement.enumerations.TokenType;
import tn.esprit.usermanagement.repositories.JwtTokenRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.EmailSender;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
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
    public String authenticate(AuthenticationRequest request) {
        var user = userRepo.findByEmail(request.getEmail()).orElse(null);
        if (user == null )
        {
            return "there is no account associated with such email! ";
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
            String link = "http://localhost:8085/api/v1/auth/confirm?token="+token;
            emailSender.send(request.getEmail(),token);
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

    public User register(RegistrationRequest request) {

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
                .twoFactorsAuth(false)
                .build();
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
        String link = "http://localhost:8085/api/v1/auth/confirm?token="+token;
        emailSender.send(request.getEmail(),buildEmail(request.getFirstName(),link));
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
            String link = "http://localhost:8085/api/v1/auth/confirm?token="+token2;
            emailSender.send(confirmationToken.getUser().getEmail(),buildEmail(confirmationToken.getUser().getFirstName(),link));
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
            //String link = "http://localhost:8085/api/v1/auth/confirm?token="+token2;
            emailSender.send(confirmationToken.getUser().getEmail(),token2);
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
        String link = "http://localhost:8085/api/v1/auth/confirm?token="+token;
        emailSender.send(user.getEmail(),token);
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
            //String link = "http://localhost:8085/api/v1/auth/confirm?token="+token2;
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

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
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
