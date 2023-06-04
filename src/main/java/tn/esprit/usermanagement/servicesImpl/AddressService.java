package tn.esprit.usermanagement.servicesImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import lombok.AllArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.dto.GeocodeResult;
import tn.esprit.usermanagement.entities.Address;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.AddressRepo;
import tn.esprit.usermanagement.repositories.UserRepo;

import java.io.IOException;
import java.net.URLEncoder;

@Service
@AllArgsConstructor
public class AddressService {
    private AddressRepo addressRepo;
    public UserRepo userRepo;


    public Address AddAddress(String address) throws IOException {

        OkHttpClient client = new OkHttpClient();
        //String encodedAddress = URLEncoder.encode(address, "UTF-8");
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
    public String retrouveradresseUSER(Integer idUser){
        User user =userRepo.findById2(idUser);

        String s=getAddress(user.getLatitude(),user.getLongitude());
        user.setAdresse(s);
        return  s;
    }

    public String getAddress(double latitude, double longitude) {
        try {
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey("caf83ab54amsh7c09f9cfec15ca6p15df8djsnb130792f6d31")
                    .build();

            GeocodingResult[] results = GeocodingApi.reverseGeocode(context, new LatLng(latitude, longitude)).await();
            if (results.length > 0) {
                return results[0].formattedAddress;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}