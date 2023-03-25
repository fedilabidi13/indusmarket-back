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
    private ProductWishlistRepository wishlistRepository;
    private AuthenticationService authenticationService;
    private UserRepo userRepo;

    @Override
    public Product addToWishlist(Product product) {
        Integer idUser = authenticationService.currentlyAuthenticatedUser().getId();
        User user = userRepo.findById(idUser).get();
        boolean productExistsInWishlist = wishlistRepository.findByUserAndProduct(user, product).isPresent();

        if (productExistsInWishlist) {
            // Throw an exception if the product is already in the user's wishlist
            throw new IllegalStateException("Product is already in wishlist");
        }

        // Add the product to the user's wishlist
        ProductWishlist wishlistItem = new ProductWishlist(product, user);
        wishlistRepository.save(wishlistItem);
        return product;
    }

    @Override
    public List<ProductWishlist> getWishlist() {
        Integer idUser = authenticationService.currentlyAuthenticatedUser().getId();
        User user = userRepo.findById(idUser).get();
        return wishlistRepository.findByUser(user);
    }

    @Override
    public ProductWishlist removeFromWishlist(ProductWishlist wishlistItem) {
        ProductWishlist wishlist = wishlistItem;
        wishlistRepository.delete(wishlistItem);
        return wishlist;
    }
}
