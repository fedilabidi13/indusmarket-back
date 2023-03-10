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
    @PostMapping("/add/{id}")
    public ShoppingCart addingShoppingCart(@PathVariable("id") Integer id)
    {
        return shoppingCartService.create(id);
    }
    @GetMapping("/load-items")
    public List<CartItem> AllCartItems(@RequestParam Integer id)
    {
        return shoppingCartService.loadCartItem(id);
    }
    @DeleteMapping("/deleteShoppingCart")
    void deleteShoppingCart (@RequestParam Integer idShoppingCart) {
          shoppingCartService.deleteShoppingCart(idShoppingCart);
    }



    //    @GetMapping("/recommendations/{userId}")
//    public List<Product> getRecommendations(@PathVariable Integer userId){ return shoppingCartService.getRecommendationsForUser(userId);}
}
