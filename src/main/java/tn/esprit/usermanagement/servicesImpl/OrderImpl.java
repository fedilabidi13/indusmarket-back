package tn.esprit.usermanagement.servicesImpl;

import tn.esprit.usermanagement.entities.CartItem;
import tn.esprit.usermanagement.entities.Orders;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.repositories.ProductRepo;
import tn.esprit.usermanagement.repositories.ShoppingCartRepo;
import tn.esprit.usermanagement.services.IOrderService;
import tn.esprit.usermanagement.repositories.OrderRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
@Service
@AllArgsConstructor
public class OrderImpl implements IOrderService {
    private OrderRepo orderRepo;

    private ProductRepo productRepo;
    private final UserRepo userRepo;

    private ShoppingCartRepo shoppingCartRepo;

    //************************************** order Creation ************************************//

    @Override
   public Orders createOrder(Integer idUser) {

        Orders order = new Orders();
        order.setCreationDate(LocalDateTime.now());
        order.setUser(userRepo.getReferenceById(idUser));
        order.setPaid(false);
        order.setSecondCartItemList(new ArrayList<>());

        float amount =0;
        for (CartItem cartItem: order.getUser().getShoppingCart().getCartItemList())
        {
            order.getSecondCartItemList().add(cartItem);
            if (cartItem.getProduct().getPriceAfterDiscount()==0){
                amount+= cartItem.getProduct().getPrice()* cartItem.getQuantity();
            }
            else  { amount+= cartItem.getProduct().getPriceAfterDiscount()* cartItem.getQuantity(); }
        }
        order.setTotalAmount(amount);

        return  orderRepo.save(order);


    }

    @Override
    public Orders updateOrder(Integer idOrder) {
        // todo update Order
        return null;
    }

    //************************************* Calcule amount ***************************************//



    //**************************************** delete order ********************************//


    public void deleteOrder(Integer orderId)  {

        Orders order = orderRepo.findById(orderId).get();

        LocalDateTime creationTime = order.getCreationDate();
        LocalDateTime currentTime = LocalDateTime.now();
        long hoursSinceCreation = ChronoUnit.HOURS.between(creationTime, currentTime);

        if (hoursSinceCreation >= 24) {
            System.err.println("the order is in charge you can not delete it");
        }
        else {
        orderRepo.delete(order); }
    }






}
