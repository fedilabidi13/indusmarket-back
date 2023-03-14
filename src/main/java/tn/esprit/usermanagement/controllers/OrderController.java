package tn.esprit.usermanagement.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.CartItem;
import tn.esprit.usermanagement.entities.Orders;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.services.IInvoiceService;
import tn.esprit.usermanagement.services.IOrderService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/order")
@AllArgsConstructor

public class OrderController {
    private IOrderService orderService;


    @PostMapping("/add")
    public Orders createOrder() {
        return orderService.createOrder();
    }

    @DeleteMapping("/delete")
    public void deleteOrder(@RequestParam Integer orderId) {

        orderService.deleteOrder(orderId);
    }


    @PostMapping("/update")
    public void updateOrder(@RequestParam Integer idOrder, @RequestParam String dilevryAdresse) {
        orderService.updateOrder(idOrder, dilevryAdresse);
    }


    @GetMapping("/recommendProduct")
    public Product recommendProduct() {
      return   orderService.recommendProduct();
    }





}
