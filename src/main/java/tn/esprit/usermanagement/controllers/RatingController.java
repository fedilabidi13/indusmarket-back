package tn.esprit.usermanagement.controllers;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.Rating;
import tn.esprit.usermanagement.repositories.ShopRepo;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;
import tn.esprit.usermanagement.servicesImpl.RateService;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/Rating")

public class RatingController {

    @Autowired
    private RateService rateService;
    @Autowired
    private ShopRepo shopRepo;
    private final AuthenticationService authenticationService;
    @PostMapping("/rates/{shopId}")
    public Rating createRate(@PathVariable("shopId") Integer shopId , @RequestBody Rating ra) {
        Integer userId = authenticationService.currentlyAuthenticatedUser().getId();
        return rateService.createRate(ra,shopId,userId);

    }
    @GetMapping("/getAllRatesForShop")
    public ResponseEntity<List<Rating>> getAllRatesForShop(@PathVariable Integer shopId) {
       List<Rating> rates = rateService.getAllRatesForShop(shopId);
        return ResponseEntity.ok(rates);
   }

    @GetMapping("/average-rating")
    public ResponseEntity<Double> getAverageRatingForShop(@PathVariable Integer shopId) {
        Double averageRating = rateService.getAverageRatingForShop(shopId);
        return ResponseEntity.ok(averageRating);
    }

}


