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


    /******************************** Create shoppingCartNotUse ****************************/

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


    //************************************* Show all the cartitems in a shopping cart *********************//
    @Override
    public List<CartItem> loadCartItem(Integer idUser) {
        User user = userRepo.getReferenceById(idUser);
        return user.getShoppingCart().getCartItemList();

    }


    //********************************** Delete a shoppingCart ******************************** //


    @Override
    public  void deleteShoppingCart ( Integer idShoppingCart){
        ShoppingCart shoppingCart = shoppingCartRepo.getReferenceById(idShoppingCart);
        shoppingCartRepo.delete(shoppingCart);
    }





   /* @Override
    public List<Product> getRecommendationsForUser(Integer userId) {

        User user = userRepo.getReferenceById(userId);
        ShoppingCart shoppingCart = user.getShoppingCart();
        List<CartItem> cartItems = shoppingCart.getCartItemList();

        Map<Category, List<CartItem>> cartItemsByCategory = cartItems.stream().collect(Collectors.groupingBy(cartItem -> cartItem.getProduct().getCategory()));

        List<Product> recommendedProducts = new ArrayList<>();
        cartItemsByCategory.forEach((category, items) -> {List<Product> products = productRepo.findByCategory(category);
            if (!products.isEmpty()) {
                int randomIndex = new Random().nextInt(products.size());
                recommendedProducts.add(products.get(randomIndex));
            }
        });
        return recommendedProducts;
    }
*/

}
