package tn.esprit.usermanagement.servicesImpl;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.entities.Rating;
import tn.esprit.usermanagement.entities.Shop;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.RateRepository;
import tn.esprit.usermanagement.repositories.ShopRepo;
import tn.esprit.usermanagement.repositories.UserRepo;

import java.util.List;
@AllArgsConstructor
@Service
public class RateService {

    private RateRepository rateRepository;
private ShopRepo shopRepo;
private UserRepo userRepo;

    public Rating createRate(Rating ra, Integer ShopId, Integer UserId) {
        User user = userRepo.findById(UserId).get();
        Rating rate = rateRepository.save(ra);
        Shop shop = shopRepo.findById(ShopId).get();
        rate.setShop(shop);
       rate.setUser1(user);
        userRepo.save(user);
        return rateRepository.save(rate);
    }

    public List<Rating> getAllRatesForShop(Integer shopId) {
        return rateRepository.findByShopId(shopId);
   }

    public Double getAverageRatingForShop(Integer shopId) {
        return rateRepository.getAverageRatingForShop(shopId);
    }
}

