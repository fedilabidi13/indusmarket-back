package tn.esprit.usermanagement.services;

import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.entities.ProductWishlist;
import tn.esprit.usermanagement.entities.User;

import java.util.List;

public interface IWishList {
    public void addToWishlist(Product product);
    public List<ProductWishlist> getWishlist();
    public void removeFromWishlist(ProductWishlist wishlistItem);
}
