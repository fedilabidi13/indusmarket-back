package tn.esprit.usermanagement.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.entities.ProductWishlist;
import tn.esprit.usermanagement.repositories.ProductRepo;
import tn.esprit.usermanagement.repositories.ProductWishlistRepository;
import tn.esprit.usermanagement.servicesImpl.ProductWishlistService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/wishlist")
public class ProductWishlistController {

    private ProductWishlistService wishlistService;
    private ProductRepo productRepo;
    private ProductWishlistRepository productWishlistRepository;

    @PostMapping("/add")
    public Product addToWishlist(@RequestParam("productId") int productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return wishlistService.addToWishlist(product);
    }

    @GetMapping("/getWishlist")
    public List<ProductWishlist> getWishlist() {
        return wishlistService.getWishlist();
    }

    @DeleteMapping("/remove")
    public ProductWishlist removeFromWishlist(@RequestParam ("id") int id) {

        return wishlistService.removeFromWishlist(productWishlistRepository.getReferenceById(id));
    }
}
