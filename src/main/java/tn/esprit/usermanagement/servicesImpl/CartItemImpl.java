package tn.esprit.usermanagement.servicesImpl;

import org.springframework.transaction.annotation.Transactional;
import tn.esprit.usermanagement.entities.CartItem;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.entities.ShoppingCart;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.ShoppingCartRepo;
import tn.esprit.usermanagement.services.ICartItemService;
import tn.esprit.usermanagement.repositories.CartItemRepo;
import tn.esprit.usermanagement.repositories.ProductRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartItemImpl implements ICartItemService {
    private UserRepo userRepo;
    private CartItemRepo cartItemRepo;
    private final ProductRepo productRepo;
    private final ShoppingCartRepo shoppingCartRepo;

    @Override
    @Transactional
    public CartItem addAndAssignToCart(Integer idProduct, Integer qte, Integer idUser) {
        User user = userRepo.getReferenceById(idUser);
        Product product = productRepo.getReferenceById(idProduct);


        if (product.getQuantity() > 0 && product.getQuantity()>=qte ) {
            if (user.getShoppingCart() == null) {
                ShoppingCart newShoppingcart = new ShoppingCart();
                user.setShoppingCart(newShoppingcart);
                CartItem cartItem = new CartItem();
                cartItem.setShoppingCart(newShoppingcart);
                cartItem.setQuantity(qte);
                cartItem.setProduct(product);
                shoppingCartRepo.save(newShoppingcart);
                cartItemRepo.save(cartItem);
                return cartItem;

            } else {
                ShoppingCart shoppingCart = user.getShoppingCart();
                CartItem cartItem = new CartItem();
                cartItem.setShoppingCart(shoppingCart);
                cartItem.setQuantity(qte);
                cartItem.setProduct(product);
                shoppingCartRepo.save(shoppingCart);
                cartItemRepo.save(cartItem);
                return cartItem;
            }
        } else {
            //throw new RuntimeException("Error: this product is not available anymore");
            System.err.println("romena il mazyouna tahfouna");
            }
             return null;
    }

    @Override
    public void updateCartItemQuantity(Integer cartItemId,Integer counterValue) {
        CartItem theCart = cartItemRepo.getReferenceById(cartItemId);
        int currentQuantity = theCart.getQuantity();

        if (theCart.getProduct().getQuantity() > 0 && currentQuantity >= 1) {
            if (counterValue == 1 || counterValue == -1) {
                int newQuantity = currentQuantity + counterValue;
                theCart.setQuantity(newQuantity);
            } else {
                throw new RuntimeException("Error: this product is not available anymore");
            }
            cartItemRepo.save(theCart);

        }}

        @Override
            public void deleteCartItemAndRemoveFromShoppingCart(Integer idCartItem){
            CartItem TheCartItem = cartItemRepo.getReferenceById(idCartItem);
            ShoppingCart shoppingCart = TheCartItem.getShoppingCart();
            shoppingCart.removeCartItem(TheCartItem);
            cartItemRepo.delete(TheCartItem);
            shoppingCartRepo.save(shoppingCart);

        }

        @Override
        public CartItem AfficherCartItem (Integer idCartItem){

        CartItem cartItem = cartItemRepo.getReferenceById(idCartItem);

         cartItem.getProduct();

         return  cartItem;
    }
    }












