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
    @ResponseBody
    public CartItem dfghjkl(@RequestParam Integer product, @RequestParam Integer qte, @RequestParam Integer id)
    {
        return cartItemService.addAndAssignToCart(product,qte,id);
    }
}
