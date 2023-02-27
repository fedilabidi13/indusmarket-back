package tn.esprit.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class GeocodeGeometry {
    @JsonProperty("location")
    GeocodeLocation geocodeLocation;
    public GeocodeGeometry() {
    }
    public GeocodeLocation getGeocodeLocation() {
        return geocodeLocation;
    }
    public void setGeocodeLocation(GeocodeLocation geocodeLocation) {
        this.geocodeLocation = geocodeLocation;
    }
}
