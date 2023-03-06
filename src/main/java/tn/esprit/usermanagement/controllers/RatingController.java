package tn.esprit.usermanagement.controllers;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.Rating;
import tn.esprit.usermanagement.entities.Shop;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.RateRepository;
import tn.esprit.usermanagement.repositories.ReactRepo;
import tn.esprit.usermanagement.repositories.ShopRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/Rating")

public class RatingController {

    @Autowired
    private ShopRepo shopRepo;
    @Autowired
    AuthenticationService authenticationService;
    private final UserRepo userRepo;
    private final RateRepository rateRepository;
    private final ReactRepo reactRepo;

    @PostMapping("/rates/{shopId}")
    public Rating createRate(@PathVariable("shopId") Integer shopId , @RequestBody Rating ra) {
        Integer idUser = authenticationService.currentlyAuthenticatedUser().getId();
        User user = userRepo.findById2(idUser);
        Rating rate = rateRepository.save(ra);
        Shop shop = shopRepo.findById2(shopId);
        rate.setShop(shop);
        rate.setUser1(user);
        userRepo.save(user);
        return rateRepository.save(rate);


    }
    @GetMapping("/getAllRatesForShop/{shopId}")
    public List<Rating> getAllRatesForShop(@PathVariable("shopId") Integer shopId) {
       return rateRepository.findByShopId(shopId);
   }

    @GetMapping("/average-rating/{shopId}")
    public Double getAverageRatingForShop(@PathVariable("shopId") Integer shopId) {
        return rateRepository.getAverageRatingForShop(shopId);
    }

}


