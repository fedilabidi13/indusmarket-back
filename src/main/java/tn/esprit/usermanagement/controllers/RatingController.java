package tn.esprit.usermanagement.controllers;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.Rating;
import tn.esprit.usermanagement.servicesImpl.RatingServiceImpl;

import java.util.List;
@AllArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/rating")
public class RatingController {
    private RatingServiceImpl ratingService;

    @PostMapping("/add/{shopId}/{value}")
    public Rating createRate(@PathVariable ("shopId") Integer shopId ,@PathVariable("value") int value) {
        return ratingService.createRate(shopId,value);
    }
    @GetMapping("/findByShop/{shopId}")
    public List<Rating> getAllRatesForShop(@PathVariable("shopId") Integer shopId) {
       return ratingService.getAllRatesForShop(shopId);
   }

    @GetMapping("/shopAverage/{shopId}")
    public Double getAverageRatingForShop(@PathVariable("shopId") Integer shopId) {
        return ratingService.getAverageRatingForShop(shopId);
    }
    @GetMapping("/getRatingByUser/{shopId}")
    public double getAverageRatingForShopByUser(@PathVariable("shopId") Integer shopId){
        return ratingService.getRatingByUserAndShopByUser(shopId);
    }
}


