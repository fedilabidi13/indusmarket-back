package tn.esprit.usermanagement.servicesImpl;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TwilioService {
    // Find your Account SID and Auth Token at twilio.com/console
    // and set the environment variables. See http://twil.io/secure
    public static final String ACCOUNT_SID = "ACac4b5a1492f2cf01506ff9cee0390a5d";
    public static final String AUTH_TOKEN = "829e314efd34d2eb704570aa123f5668";

    public void sendCode(String numTel, String telToken) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("+216"+numTel),
                        new com.twilio.type.PhoneNumber("+14406933927"),
                        "Verification code: "+telToken)
                .create();

    }
    public String generateCode() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
}
