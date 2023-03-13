package tn.esprit.usermanagement.dto;

import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
public class GeolocationApiResponse {
    private List<GeolocationFeature> features;
}
