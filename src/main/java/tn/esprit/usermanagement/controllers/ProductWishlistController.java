package tn.esprit.usermanagement.controllers;

import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.entities.ProductWishlist;
import tn.esprit.usermanagement.repositories.ProductRepo;
import tn.esprit.usermanagement.servicesImpl.ProductWishlistService;

import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class ProductWishlistController {

    private ProductWishlistService wishlistService;
    private ProductRepo productRepo;

    @PostMapping("/add")
    public void addToWishlist(@RequestParam int productId) {
        Product product = productRepo.findById(productId).get();
        wishlistService.addToWishlist(product);
    }

    @GetMapping("/getWishlist")
    public List<ProductWishlist> getWishlist() {
        return wishlistService.getWishlist();
    }

    @PostMapping("/remove")
    public void removeFromWishlist(@RequestBody ProductWishlist wishlistItem) {
        wishlistService.removeFromWishlist(wishlistItem);
    }
}
