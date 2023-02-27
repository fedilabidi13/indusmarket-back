package tn.esprit.usermanagement.services;

import tn.esprit.usermanagement.entities.CartItem;
import tn.esprit.usermanagement.entities.ShoppingCart;

import java.util.List;

public interface IShoppingCartService {
    ShoppingCart create (Integer idUser);
    List<CartItem> loadCartItem(Integer idUser);
}
