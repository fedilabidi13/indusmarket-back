package tn.esprit.usermanagement.servicesImpl;

import tn.esprit.usermanagement.entities.CartItem;
import tn.esprit.usermanagement.entities.ShoppingCart;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.services.IShoppingCartService;
import tn.esprit.usermanagement.repositories.ShoppingCartRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor

public class ShoppingCartImpl implements IShoppingCartService {
    private UserRepo userRepo;
    private ShoppingCartRepo shoppingCartRepo;

    @Override
    public ShoppingCart create(Integer idUser) {
        User user = userRepo.getReferenceById(idUser);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCartItemList(new ArrayList<>());
        shoppingCartRepo.save(shoppingCart);
        user.setShoppingCart(shoppingCart);
        userRepo.save(user);
        return shoppingCart;
    }

    @Override
    public List<CartItem> loadCartItem(Integer idUser) {
        User user = userRepo.getReferenceById(idUser);
        return user.getShoppingCart().getCartItemList();
    }
}
