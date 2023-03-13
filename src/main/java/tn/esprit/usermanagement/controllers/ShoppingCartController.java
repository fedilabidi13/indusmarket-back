package tn.esprit.usermanagement.controllers;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.CartItem;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.entities.ShoppingCart;
import tn.esprit.usermanagement.services.IShoppingCartService;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@AllArgsConstructor
public class ShoppingCartController {
    private IShoppingCartService shoppingCartService;
    @PostMapping("/add")
    public ShoppingCart addingShoppingCart( )
    {
        return shoppingCartService.create();
    }
    @GetMapping("/load-items")
    public List<CartItem> AllCartItems( )
    {
        return shoppingCartService.loadCartItem( );
    }
    @DeleteMapping("/deleteShoppingCart")
    void deleteShoppingCart (@RequestParam Integer idShoppingCart) {
          shoppingCartService.deleteShoppingCart(idShoppingCart);
    }



    //    @GetMapping("/recommendations/{userId}")
//    public List<Product> getRecommendations(@PathVariable Integer userId){ return shoppingCartService.getRecommendationsForUser(userId);}
}
