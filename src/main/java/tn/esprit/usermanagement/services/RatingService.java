package tn.esprit.usermanagement.services;

import tn.esprit.usermanagement.entities.Rating;

import java.util.List;

public interface RatingService {
     Rating createRate(Integer shopId , int ratingValue);
     List<Rating> getAllRatesForShop(Integer shopId);
     Double getAverageRatingForShop( Integer shopId);
}
