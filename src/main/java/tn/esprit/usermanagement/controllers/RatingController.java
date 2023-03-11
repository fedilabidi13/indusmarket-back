package tn.esprit.usermanagement.controllers;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.Rating;
import tn.esprit.usermanagement.servicesImpl.RatingServiceImpl;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/rating")
public class RatingController {
    RatingServiceImpl ratingService;

    @PostMapping("/add}")
    public Rating createRate(@RequestParam Integer shopId , @RequestBody Rating ra) {

        return ratingService.createRate(shopId,ra);


    }
    @GetMapping("/findByShop")
    public List<Rating> getAllRatesForShop(@RequestParam Integer shopId) {
       return ratingService.getAllRatesForShop(shopId);
   }

    @GetMapping("/shopAverage")
    public Double getAverageRatingForShop(@RequestParam Integer shopId) {
        return ratingService.getAverageRatingForShop(shopId);
    }

}


