package tn.esprit.usermanagement.servicesImpl;

import tn.esprit.usermanagement.entities.CartItem;
import tn.esprit.usermanagement.entities.Orders;
import tn.esprit.usermanagement.services.IOrderService;
import tn.esprit.usermanagement.repositories.OrderRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class OrderImpl implements IOrderService {
    private OrderRepo orderRepo;
    private final UserRepo userRepo;

    @Override
    public Orders create(Integer idUser) {
        Orders order = new Orders();
        order.setUser(userRepo.getReferenceById(idUser));
        return orderRepo.save(order);
    }

    @Override
    public List<CartItem> loadItems(Integer idOrder) {
        return orderRepo.getReferenceById(idOrder).getUser().getShoppingCart().getCartItemList();
    }

    @Override
    public float calculerAmount(Integer idOrder) {
        float amount = 0;
        Orders order = orderRepo.getReferenceById(idOrder);
        List<CartItem> lista = loadItems(idOrder);
        for (CartItem item : lista)
        {
            amount+= item.getProduct().getPrice();
        }
        return amount;
    }
}
