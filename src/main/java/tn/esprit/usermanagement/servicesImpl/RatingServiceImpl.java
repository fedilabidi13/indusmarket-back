package tn.esprit.usermanagement.servicesImpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.entities.Rating;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.RateRepository;
import tn.esprit.usermanagement.repositories.ShopRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.RatingService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor

public class RatingServiceImpl implements RatingService {
   private UserRepo userRepo;
   private RateRepository rateRepository;
   private ShopRepo shopRepo;
    private AuthenticationService authenticationService;

    public Rating createRate( Integer shopId ,  Rating ra) {

        Integer idUser = authenticationService.currentlyAuthenticatedUser().getId();
        User user = userRepo.findById(idUser).get();
        // Check if the user has already rated the shop
        Rating existingRating = rateRepository.findByUser1AndShop(user, shopRepo.findById(shopId).get());
        if (existingRating != null) {
            // Update the existing rating with the new values
            existingRating.setValue(ra.getValue());
            existingRating.setRatedAt(LocalDateTime.now());
            return rateRepository.save(existingRating);
        }
        // Create a new rating for the user and shop
        ra.setRatedAt(LocalDateTime.now());
        ra.setShop(shopRepo.findById(shopId).orElse(null));
        ra.setUser1(user);
         rateRepository.save(ra);
        return ra;
    }

    public List<Rating> getAllRatesForShop(Integer shopId) {
        return rateRepository.findByShopId(shopId);
    }


    public Double getAverageRatingForShop( Integer shopId) {
        return rateRepository.getAverageRatingForShop(shopId);
    }
}
