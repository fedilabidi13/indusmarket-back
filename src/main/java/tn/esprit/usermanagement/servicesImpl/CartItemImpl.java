package tn.esprit.usermanagement.servicesImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.usermanagement.entities.*;
import tn.esprit.usermanagement.repositories.*;
import tn.esprit.usermanagement.services.ICartItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class CartItemImpl implements ICartItemService {
    private UserRepo userRepo;
    private CartItemRepo cartItemRepo;
    private final ProductRepo productRepo;
    private final ShoppingCartRepo shoppingCartRepo;

    private final ProductRequestRepo productRequestRepo;


    AuthenticationService authenticationService;

    @Override
    @Transactional
    public String addAndAssignToCart(Integer idProduct, Integer qte) {
        User user = authenticationService.currentlyAuthenticatedUser();

        Product product = productRepo.getReferenceById(idProduct);


        if (product.getStock().getCurrentQuantity() > 0 && product.getStock().getCurrentQuantity()>=qte &&  product.getShop().getUser().getEnabled()==true ) {
            if (user.getShoppingCart() == null) {
                ShoppingCart newShoppingcart = new ShoppingCart();
                user.setShoppingCart(newShoppingcart);
                CartItem cartItem = new CartItem();
                cartItem.setShoppingCart(newShoppingcart);
                cartItem.setQuantity(qte);
                cartItem.setProduct(product);
                shoppingCartRepo.save(newShoppingcart);
                cartItemRepo.save(cartItem);
                return "cartItem added !";

            } else {
                ShoppingCart shoppingCart = user.getShoppingCart();
                CartItem cartItem = new CartItem();
                cartItem.setShoppingCart(shoppingCart);
                cartItem.setQuantity(qte);
                cartItem.setProduct(product);
                shoppingCartRepo.save(shoppingCart);
                cartItemRepo.save(cartItem);
                return "cartItem added!";
            }
        } else {

            ProductRequest productRequest = new ProductRequest();
            productRequest.setProduct(product);
            productRequest.setUser(user);
            productRequest.setQuantityNeeded(qte);
            productRequestRepo.save(productRequest);

                return "Error: we have problem with the product or the buyer";
            }
        }



    @Override
    public void updateCartItemQuantity(Integer cartItemId,Integer counterValue) {
        CartItem theCart = cartItemRepo.getReferenceById(cartItemId);
        int currentQuantity = theCart.getQuantity();

        if (theCart.getProduct().getStock().getCurrentQuantity() > 0 && currentQuantity >= 1 ) {
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












