package tn.esprit.usermanagement.servicesImpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.entities.ProductWishlist;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.ProductWishlistRepository;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.IWishList;

import java.util.List;
@Service
@AllArgsConstructor
public class ProductWishlistService implements IWishList {
    ProductWishlistRepository wishlistRepository;
    AuthenticationService authenticationService;
    UserRepo userRepo;

    @Override
    public void addToWishlist(Product product) {
        Integer idUser = authenticationService.currentlyAuthenticatedUser().getId();
        User user = userRepo.findById(idUser).get();
        ProductWishlist wishlistItem = new ProductWishlist(product, user);
        wishlistRepository.save(wishlistItem);
    }

    @Override
    public List<ProductWishlist> getWishlist() {
        Integer idUser = authenticationService.currentlyAuthenticatedUser().getId();
        User user = userRepo.findById(idUser).get();
        return wishlistRepository.findByUser(user);
    }

    @Override
    public void removeFromWishlist(ProductWishlist wishlistItem) {
        wishlistRepository.delete(wishlistItem);
    }
}
