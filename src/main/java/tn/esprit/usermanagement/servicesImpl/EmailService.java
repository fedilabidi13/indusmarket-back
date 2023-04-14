package tn.esprit.usermanagement.servicesImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.services.EmailSender;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService implements EmailSender {



    private final JavaMailSender mailSender;

    @Override
    @Async
    public void send(String to, String email) {
        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("contact@indusmarket.tn");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("failed to send email "+e);

            throw new IllegalStateException("failed to send email");
        }
    }


    @Override
    @Async
    public void sendForProductRequest(String to, String email) {
        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("The Product is no more out of stock");
            helper.setFrom("contact@indusmarket.tn");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("failed to send email "+e);

            throw new IllegalStateException("failed to send email");
        }
    }
    @Override
    @Async
    public void sendClaimEmail(String to, String email) {
        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Claim Treatment");
            helper.setFrom("contact@indusmarket.tn");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("failed to send email "+e);

            throw new IllegalStateException("failed to send email");
        }
    }
    @Override
    @Async
    public void sendEventEmail(String to, String email) {
        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Events Management");
            helper.setFrom("contact@indusmarket.tn");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("failed to send email "+e);
            throw new IllegalStateException("failed to send email");
        }
    }
    @Override
    @Async
    public void sendForProductRestock(String to, String email) {
        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Product stock alert!");
            helper.setFrom("contact@indusmarket.tn");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("failed to send email "+e);

            throw new IllegalStateException("failed to send email");
        }
    }
}

