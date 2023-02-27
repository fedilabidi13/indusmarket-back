package tn.esprit.usermanagement.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.CartItem;
import tn.esprit.usermanagement.entities.Orders;
import tn.esprit.usermanagement.services.IOrderService;

import java.util.List;

@RestController
@RequestMapping("/order")
@AllArgsConstructor

public class OrderController {
    private IOrderService orderService;
    @PostMapping("/add")
    private Orders dfghnj(@RequestParam Integer id)
    {
        return orderService.create(id);
    }

    @GetMapping("/load-items")
    public List<CartItem> dfgbn(@RequestParam Integer id)
    {
        return orderService.loadItems(id);
    }
}
