package tn.esprit.usermanagement.services;

import tn.esprit.usermanagement.entities.CartItem;

public interface ICartItemService {
    CartItem addAndAssignToCart(Integer idProduct, Integer qte,Integer idUser);
    void updateCartItemQuantity(Integer cartItemId,Integer counterValue);
     void deleteCartItemAndRemoveFromShoppingCart(Integer idCartItem);


    }
