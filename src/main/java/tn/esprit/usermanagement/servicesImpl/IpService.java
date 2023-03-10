package tn.esprit.usermanagement.servicesImpl;

import com.maxmind.geoip.Country;
import com.maxmind.geoip.Location;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.juli.logging.Log;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Locale;


@Slf4j
public class IpService {
    /*
     public static void main(String[] args) {
        try (Socket socket = new Socket()) {
            String ip = Inet4Address.getLocalHost().getHostAddress();
            log.info(ip);
            HttpClient client= new HttpGet("https://www.infobyip.com/ip-"+ip+".html";
            try {


                log.info("Country is "+client.egetLocale().getCountry());
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

     */
}