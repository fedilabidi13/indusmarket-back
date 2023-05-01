package tn.esprit.usermanagement.services;

import tn.esprit.usermanagement.entities.CartItem;
import tn.esprit.usermanagement.entities.Orders;
import tn.esprit.usermanagement.entities.Product;

import java.util.List;

public interface IOrderService {

      Orders createOrder();
    //List<CartItem> loadItems(Integer idOrder);


    void updateOrder(Integer idOrder , String dilevryAdresse);

     void deleteOrder(Integer orderId);

    public Product recommendProduct();

    public List<CartItem> AfficherOneOrderByID (Integer orderId);

    public void lessQuantity (String orderId);


    public List<Orders> loadCartItemOrder();

    public List<Product> AfficherOneOrder (Integer orderId);

    public List<Orders> loadCartItemAllOrder();



}
