package tn.esprit.usermanagement.controllers;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.CartItem;
import tn.esprit.usermanagement.services.ICartItemService;

@RestController
@RequestMapping("/cartItem")
@AllArgsConstructor
public class CartItemController {
    private ICartItemService cartItemService;

    @PostMapping("/add")

    public CartItem addAndAssignToCart(@RequestParam Integer idproduct, @RequestParam Integer qte, @RequestParam Integer idUser)
    {
        return cartItemService.addAndAssignToCart(idproduct,qte,idUser);
    }

    @PutMapping("/updateCartItemQuantity")
    public void updateCartItemQuantity( @RequestParam ("cartItemId") Integer cartItemId, @RequestParam ("counterValue")Integer counterValue){
         cartItemService.updateCartItemQuantity(cartItemId ,counterValue);
    }

@DeleteMapping("/deleteCartItemAndRemoveFromCart")
    public void deleteCartItemAndRemoveFromShoppingCart( @RequestParam Integer idCartItem){
        cartItemService.deleteCartItemAndRemoveFromShoppingCart(idCartItem);
    }}
