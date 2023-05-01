package tn.esprit.usermanagement.servicesImpl;

import tn.esprit.usermanagement.entities.CartItem;
import tn.esprit.usermanagement.entities.ShoppingCart;
import tn.esprit.usermanagement.entities.User;

import tn.esprit.usermanagement.repositories.CartItemRepo;
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
    private final CartItemRepo cartItemRepo;

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
        List<CartItem> cartItems = user.getShoppingCart().getCartItemList();
        List<Long> productIdList = new ArrayList<>();
        List<CartItem> uniqueCartItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Long productId = cartItem.getProduct().getIdProduct().longValue();
            if (productIdList.contains(productId)) {
                // This product has already been added to the cart, so update the quantity
                int index = productIdList.indexOf(productId);
                uniqueCartItems.get(index).setQuantity(uniqueCartItems.get(index).getQuantity() + cartItem.getQuantity());
                cartItemRepo.delete(cartItem); // Delete the duplicate cart item from the database
            } else {
                // This is a unique product, so add it to the list
                productIdList.add(productId);
                uniqueCartItems.add(cartItem);
            }
        }

        user.getShoppingCart().setCartItemList(uniqueCartItems);
        shoppingCartRepo.save(user.getShoppingCart());
        return uniqueCartItems;
    }



    //********************************** Delete a shoppingCart ******************************** //


    @Override
    public  void deleteShoppingCart ( Integer idShoppingCart){
        ShoppingCart shoppingCart = shoppingCartRepo.getReferenceById(idShoppingCart);
        shoppingCartRepo.delete(shoppingCart);
    }



}
