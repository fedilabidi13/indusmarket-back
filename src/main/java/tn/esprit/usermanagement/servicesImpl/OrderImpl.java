package tn.esprit.usermanagement.servicesImpl;

import tn.esprit.usermanagement.entities.CartItem;
import tn.esprit.usermanagement.entities.Orders;
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
    private final UserRepo userRepo;

    private ShoppingCartRepo shoppingCartRepo;

    //************************************** order Creation ************************************//

    @Override
   public List<CartItem> createOrder(Integer idUser) {

        Orders order = new Orders();
        order.setCreationDate(LocalDateTime.now());
        order.setUser(userRepo.getReferenceById(idUser));
        orderRepo.save(order);

        List<CartItem> newCartItemList = new ArrayList<>(orderRepo.getReferenceById(idUser).getUser().getShoppingCart().getCartItemList());
        orderRepo.save(order);
        shoppingCartRepo.save(orderRepo.getReferenceById(idUser).getUser().getShoppingCart());

        float payment= calculerAmount(order.getId());
        order.setAmount(payment);
        orderRepo.getReferenceById(idUser).getUser().getShoppingCart().getCartItemList().clear();
        shoppingCartRepo.save(orderRepo.getReferenceById(idUser).getUser().getShoppingCart());


        return newCartItemList;
    }

    //************************************* Calcule amount ***************************************//

    @Override
    public float calculerAmount(Integer idOrder) {

        float amount = 0;
        List<CartItem> list = orderRepo.getReferenceById(idOrder).getUser().getShoppingCart().getCartItemList();

        for (CartItem item : list)
        {
            if (item.getProduct().getPriceAfterDiscount()==0){
                amount+= item.getProduct().getPrice()* item.getQuantity();
            }
            else  { amount+= item.getProduct().getPriceAfterDiscount()* item.getQuantity(); }
        }

        return amount; }

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
