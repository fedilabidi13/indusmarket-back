package tn.esprit.usermanagement.servicesImpl;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Country;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;


@Slf4j
@Service
public class IpService {

     public String getCountry() throws IOException, GeoIp2Exception {
         File database = new File("D:\\GeoLite2-City.mmdb");

// This reader object should be reused across lookups as creation of it is
// expensive.
         DatabaseReader reader = new DatabaseReader.Builder(database).build();

// If you want to use caching at the cost of a small (~2MB) memory overhead:
// new DatabaseReader.Builder(file).withCache(new CHMCache()).build();
         URL whatismyip = new URL("http://checkip.amazonaws.com");
         BufferedReader in = new BufferedReader(new InputStreamReader(
                 whatismyip.openStream()));

         String ip = in.readLine();         InetAddress ipAddress = InetAddress.getByName(ip);

         CityResponse response = reader.city(ipAddress);

         Country country = response.getCountry();
         return country.getIsoCode();

    }


}