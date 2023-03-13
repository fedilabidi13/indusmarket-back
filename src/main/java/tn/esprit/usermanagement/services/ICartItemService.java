package tn.esprit.usermanagement.services;

import tn.esprit.usermanagement.entities.CartItem;

public interface ICartItemService {
    String addAndAssignToCart(Integer idProduct, Integer qte);
    void updateCartItemQuantity(Integer cartItemId,Integer counterValue);
     void deleteCartItemAndRemoveFromShoppingCart(Integer idCartItem);

     CartItem AfficherCartItem (Integer idCartItem);


    }
