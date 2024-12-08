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
@CrossOrigin(origins = "*")
@AllArgsConstructor

public class OrderController {
    private IOrderService orderService;


    @PostMapping("/add")
    public Orders createOrder() {
        return orderService.createOrder();
    }

    @PostMapping("/delete")
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

    @GetMapping("/orderList")
    public List<Orders> loadCartItemOrder( ){ return orderService.loadCartItemOrder();}

    @GetMapping("/AllorderList")
    public List<Orders> loadCartItemAllOrder(){return orderService.loadCartItemAllOrder();}


    @GetMapping("/TheOrder")
    public List<Product> AfficherOneOrder (@RequestParam Integer orderId){
        return orderService.AfficherOneOrder(orderId);
    }


    //this is for the checkout
    @GetMapping("/TheOrderbyId")
    public List<CartItem> AfficherOneOrderByID (@RequestParam Integer orderId){
        return orderService.AfficherOneOrderByID(orderId);
    }



}
