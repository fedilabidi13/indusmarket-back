package tn.esprit.usermanagement.servicesImpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service

public class EmailValidator {
    public Boolean validateEmail(String email)
    {

        //Regular Expression
        String regex = "^(.+)@(.+)$";
        //Compile regular expression to get the pattern
        Pattern pattern = Pattern.compile(regex);
        //Iterate emails array list

            //Create instance of matcher
           Matcher matcher =pattern.matcher(email);
          /* if (matcher.toString()=="true")
               return true;
           else
               return false;

           */
        return true;
    }
}
