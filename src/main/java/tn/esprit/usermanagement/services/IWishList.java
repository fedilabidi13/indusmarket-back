package tn.esprit.usermanagement.services;

import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.entities.ProductWishlist;

import java.util.List;

public interface IWishList {
     Product addToWishlist(Product product);
     List<ProductWishlist> getWishlist();
     ProductWishlist removeFromWishlist(ProductWishlist wishlistItem);
}
