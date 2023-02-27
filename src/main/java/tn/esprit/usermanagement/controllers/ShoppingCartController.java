package tn.esprit.usermanagement.controllers;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.CartItem;
import tn.esprit.usermanagement.entities.ShoppingCart;
import tn.esprit.usermanagement.services.IShoppingCartService;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@AllArgsConstructor
public class ShoppingCartController {
    private IShoppingCartService shoppingCartService;
    @PostMapping("/add/{id}")
    public ShoppingCart gbhnjk(@PathVariable("id") Integer id)
    {
        return shoppingCartService.create(id);
    }
    @GetMapping("/load-items")
    public List<CartItem> dfghjklvg(@RequestParam Integer id)
    {
        return shoppingCartService.loadCartItem(id);
    }
}
