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

    AuthenticationService  authenticationService;

    /******************************** Create shoppingCartNotUse ****************************/

    @Override
    public ShoppingCart create() {

        User user = authenticationService.currentlyAuthenticatedUser();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCartItemList(new ArrayList<>());
        shoppingCartRepo.save(shoppingCart);
        user.setShoppingCart(shoppingCart);
        userRepo.save(user);
        return shoppingCart;
    }


    //************************************* Show all the cartitems in a shopping cart *********************//
    @Override
    public List<CartItem> loadCartItem( ) {
        User user = authenticationService.currentlyAuthenticatedUser();
        return user.getShoppingCart().getCartItemList();

    }


    //********************************** Delete a shoppingCart ******************************** //


    @Override
    public  void deleteShoppingCart ( Integer idShoppingCart){
        ShoppingCart shoppingCart = shoppingCartRepo.getReferenceById(idShoppingCart);
        shoppingCartRepo.delete(shoppingCart);
    }



}
