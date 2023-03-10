package tn.esprit.usermanagement.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tn.esprit.usermanagement.entities.Invoice;
import tn.esprit.usermanagement.entities.Orders;
import tn.esprit.usermanagement.entities.ShoppingCart;
import tn.esprit.usermanagement.repositories.InvoiceRepo;
import tn.esprit.usermanagement.repositories.OrderRepo;
import tn.esprit.usermanagement.services.IInvoiceService;
import tn.esprit.usermanagement.services.IOrderService;

import java.io.IOException;

@Controller

public class CheckoutController {

    @Autowired
    private IOrderService orderService;
    @Value("${STRIPE_PUBLIC_KEY}")
    public String stripePublicKey;
    @Autowired
    private OrderRepo orderRepo;
    @GetMapping("/checkout")
    public String checkout (Model model, @RequestParam(required = true) String orderId)
    {

        Integer amountint = Integer.valueOf((int) (orderRepo.getReferenceById(Integer.valueOf(orderId)).getTotalAmount()));
        model.addAttribute("amount",amountint);
        model.addAttribute("stripePublicKey",stripePublicKey);
        model.addAttribute("currency","USD");
        model.addAttribute("orderId",orderId);
        Orders orderpaid = orderRepo.getReferenceById(Integer.valueOf(orderId));
        orderpaid.setPaid(true);
        // diminuer la quantite des produits

        orderRepo.save(orderpaid);
        return "checkout";
    }





}
