package tn.esprit.usermanagement.servicesImpl;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PasswordGenerator {
    public String generatePassword() {
        String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String specialCharacters = "!@#$";
        String numbers = "1234567890";
        String combinedChars = capitalCaseLetters + lowerCaseLetters + specialCharacters + numbers;
        Random random = new Random();
        String password = "IM";

        password+= lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        password += capitalCaseLetters.charAt(random.nextInt(capitalCaseLetters.length()));
        password += specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        password += numbers.charAt(random.nextInt(numbers.length()));

        for(int i = 0; i< 5 ; i++) {
            password += combinedChars.charAt(random.nextInt(combinedChars.length()));
        }
        return password;
    }
}
