package tn.esprit.usermanagement.servicesImpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.entities.ConfirmationToken;
import tn.esprit.usermanagement.entities.PhoneToken;
import tn.esprit.usermanagement.repositories.ConfirmationTokenRepo;
import tn.esprit.usermanagement.repositories.PhoneTokenRepo;

import java.time.LocalDateTime;
import java.util.Optional;
@Service
@AllArgsConstructor
public class PhoneTokenService {
    private final PhoneTokenRepo phoneTokenRepo;
    public void saveConfirmationToken(PhoneToken token){
        phoneTokenRepo.save(token);
    }
    public Optional<PhoneToken> getToken(String token) {
        return phoneTokenRepo.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return phoneTokenRepo.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
