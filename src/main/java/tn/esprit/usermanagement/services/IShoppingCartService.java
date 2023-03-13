package tn.esprit.usermanagement.services;

import tn.esprit.usermanagement.entities.CartItem;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.entities.ShoppingCart;

import java.util.List;

public interface IShoppingCartService {
    ShoppingCart create ( );
    List<CartItem> loadCartItem( );

      void deleteShoppingCart ( Integer idShoppingCart);


        // public List<Product> getRecommendationsForUser(Integer userId);


}
