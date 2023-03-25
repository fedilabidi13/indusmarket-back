package tn.esprit.usermanagement.controllers;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.Rating;
import tn.esprit.usermanagement.servicesImpl.RatingServiceImpl;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/rating")
public class RatingController {
    private RatingServiceImpl ratingService;

    @PostMapping("/add")
    public Rating createRate(@RequestParam("shopId") Integer shopId , @ModelAttribute Rating ra) {
        return ratingService.createRate(shopId,ra);
    }
    @GetMapping("/findByShop")
    public List<Rating> getAllRatesForShop(@RequestParam("shopId") Integer shopId) {
       return ratingService.getAllRatesForShop(shopId);
   }

    @GetMapping("/shopAverage")
    public Double getAverageRatingForShop(@RequestParam("shopId") Integer shopId) {
        return ratingService.getAverageRatingForShop(shopId);
    }

}


