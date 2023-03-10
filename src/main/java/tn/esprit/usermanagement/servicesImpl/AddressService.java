package tn.esprit.usermanagement.servicesImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.dto.GeocodeResult;
import tn.esprit.usermanagement.entities.Address;
import tn.esprit.usermanagement.repositories.AddressRepo;

import java.io.IOException;
import java.net.URLEncoder;

@Service
@AllArgsConstructor
public class AddressService {
    private AddressRepo addressRepo;
    public Address AddAddress(String address) throws IOException {

        OkHttpClient client = new OkHttpClient();
        String encodedAddress = URLEncoder.encode(address, "UTF-8");
        Request request = new Request.Builder()
                .url("https://google-maps-geocoding.p.rapidapi.com/geocode/json?address="+address)
                .get()
                .addHeader("X-RapidAPI-Key", "caf83ab54amsh7c09f9cfec15ca6p15df8djsnb130792f6d31")
                .addHeader("X-RapidAPI-Host", "google-maps-geocoding.p.rapidapi.com")
                .build();
        ResponseBody responseBody = client.newCall(request).execute().body();
        ObjectMapper objectMapper = new ObjectMapper();
        GeocodeResult result = objectMapper.readValue(responseBody.string(), GeocodeResult.class);
        Address a = new Address();
        a.setLongitude(result.getResults().get(0).getGeometry().getGeocodeLocation().getLongitude());
        a.setLatitude(result.getResults().get(0).getGeometry().getGeocodeLocation().getLatitude());
        a.setReal_Address(result.getResults().get(0).getFormattedAddress());
        return a;
    }
}