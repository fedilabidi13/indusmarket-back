package tn.esprit.usermanagement.services;

import tn.esprit.usermanagement.entities.CartItem;
import tn.esprit.usermanagement.entities.Orders;

import java.util.List;

public interface IOrderService {
  //  Orders create(Integer idUser);
    //List<CartItem> loadItems(Integer idOrder);

    Orders createOrder(Integer idOrder);

    Orders updateOrder(Integer idOrder);

     void deleteOrder(Integer orderId);



}
