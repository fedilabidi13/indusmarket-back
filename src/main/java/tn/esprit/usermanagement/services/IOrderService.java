package tn.esprit.usermanagement.services;

import tn.esprit.usermanagement.entities.CartItem;
import tn.esprit.usermanagement.entities.Orders;

import java.util.List;

public interface IOrderService {
    Orders create(Integer idUser);
    List<CartItem> loadItems(Integer idOrder);

    float calculerAmount(Integer idOrder);
}
