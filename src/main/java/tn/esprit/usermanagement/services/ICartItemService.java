package tn.esprit.usermanagement.services;

import tn.esprit.usermanagement.entities.CartItem;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.enumerations.Category;

import java.util.Map;

public interface ICartItemService {
    String addAndAssignToCart(Integer idProduct, Integer qte);
    void updateCartItemQuantity(Integer cartItemId,Integer counterValue);
     void deleteCartItemAndRemoveFromShoppingCart(Integer idCartItem);

     CartItem AfficherCartItem (Integer idCartItem);
    public Map<String, Integer> getCartItemCountByCategory();

    public Map<Category, Product> getMostCommonProductByCategory();


    }
