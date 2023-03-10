package tn.esprit.usermanagement.services;

import tn.esprit.usermanagement.entities.Rating;

import java.util.List;

public interface RatingService {
    public Rating createRate(Integer shopId , Rating ra);
    public List<Rating> getAllRatesForShop(Integer shopId);
    public Double getAverageRatingForShop( Integer shopId);
}
