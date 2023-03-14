package tn.esprit.usermanagement.servicesImpl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.entities.Rating;
import tn.esprit.usermanagement.entities.Shop;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.RateRepository;
import tn.esprit.usermanagement.repositories.ShopRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.RatingService;

import java.util.List;

@Service
@AllArgsConstructor

public class RatingServiceImpl implements RatingService {
   UserRepo userRepo;
   RateRepository rateRepository;
   ShopRepo shopRepo;
    AuthenticationService authenticationService;

    public Rating createRate( Integer shopId ,  Rating ra) {

        Integer idUser = authenticationService.currentlyAuthenticatedUser().getId();
        User user = userRepo.findById(idUser).get();
        System.err.println(idUser);
        Rating rate = rateRepository.save(ra);
        Shop shop = shopRepo.findById(shopId).get();
        rate.setShop(shop);
        rate.setUser1(user);
        userRepo.save(user);
        return rateRepository.save(rate);
    }

    public List<Rating> getAllRatesForShop(Integer shopId) {
        return rateRepository.findByShopId(shopId);
    }


    public Double getAverageRatingForShop( Integer shopId) {
        return rateRepository.getAverageRatingForShop(shopId);
    }
}
