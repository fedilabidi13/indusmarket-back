package tn.esprit.usermanagement.servicesImpl;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RefGenerator {
    public String generateRef()
    {
        String ref = "";
        ref += "IM@";
        for (int i=0; i< 8;i++)
        {
            Random random = new Random();
            ref += random.nextInt(9);

        }
        return ref;
    }
}
