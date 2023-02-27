package tn.esprit.usermanagement.servicesImpl;

import tn.esprit.usermanagement.entities.CartItem;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.entities.ShoppingCart;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.services.ICartItemService;
import tn.esprit.usermanagement.repositories.CartItemRepo;
import tn.esprit.usermanagement.repositories.ProductRepo;
import tn.esprit.usermanagement.repositories.ShoppingCartRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartItemImpl implements ICartItemService {
    private UserRepo userRepo;
    private CartItemRepo cartItemRepo;
    private ShoppingCartRepo shoppingCartRepo;
    private final ProductRepo productRepo;

    @Override
    public CartItem addAndAssignToCart(Integer idProduct, Integer qte, Integer idUser) {
        User user = userRepo.getReferenceById(idUser);
        ShoppingCart shoppingCart = user.getShoppingCart();

        Product product = productRepo.getReferenceById(idProduct);
        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setQuantity(qte);
        cartItem.setProduct(product);
        cartItemRepo.save(cartItem);

        return cartItem;
    }
}
