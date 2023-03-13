package tn.esprit.usermanagement.servicesImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tn.esprit.usermanagement.dto.GeolocationApiResponse;
import tn.esprit.usermanagement.dto.GeolocationData;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.UserRepo;

import java.util.List;
@Service
@AllArgsConstructor
public class GeolocationService {
    @Autowired
    public GeoApiContext context;


    private static final String GEOLOCATION_API_URL = "https://api.geoapify.com/v1/geocode/search?text=%s&apiKey=%s";
    private static final String GEOLOCATION_API_KEY = "AIzaSyBDWFggOS_QkAerkB30xdUSd6tYIFEKvso";

    public GeoApiContext getContext() {
        return context;
    }

    private RestTemplate restTemplate;

    public GeolocationService() {
        restTemplate = new RestTemplate();
    }

    public GeolocationData getGeolocation(String address) {
        String url = String.format(GEOLOCATION_API_URL, address, GEOLOCATION_API_KEY);
        GeolocationApiResponse response = restTemplate.getForObject(url, GeolocationApiResponse.class);
        if (response != null && response.getFeatures() != null && response.getFeatures().size() > 0) {
            return response.getFeatures().get(0).getProperties().getGeolocation();
        }
        return null;
    }

    public double calculateDistance(GeolocationData geolocation1, GeolocationData geolocation2) {
        double lat1 = geolocation1.getLatitude();
        double lon1 = geolocation1.getLongitude();
        double lat2 = geolocation2.getLatitude();
        double lon2 = geolocation2.getLongitude();
        double earthRadius = 6371; //km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;
        return distance;
    }


    public double calculateDistance(double startLat, double startLng, double endLat, double endLng) throws JsonProcessingException {
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + startLat + "," + startLng + "&destinations=" + endLat + "," + endLng + "&key=API_KEY";

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);

        JsonNode rootNode = new ObjectMapper().readTree(result);
        JsonNode distanceNode = rootNode.path("rows").get(0).path("elements").get(0).path("distance").path("value");

        return distanceNode.asDouble();
    }




}








