package tn.esprit.usermanagement.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tn.esprit.usermanagement.entities.*;
import tn.esprit.usermanagement.repositories.*;
import tn.esprit.usermanagement.services.IInvoiceService;
import tn.esprit.usermanagement.services.IOrderService;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;

import java.io.IOException;

@Controller

public class CheckoutController {

    @Autowired
    private IOrderService orderService;
    @Value("${STRIPE_PUBLIC_KEY}")
    public String stripePublicKey;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private CartItemRepo cartItemRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private ShoppingCartRepo shoppingCartRepo;
    @Autowired
    private UserRepo userRepo;

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
        orderRepo.save(orderpaid);
        orderService.lessQuantity(orderId);
       // orderRepo.save(orderpaid);
        shoppingCartRepo.delete(orderpaid.getUser().getShoppingCart());
        return "checkout";
    }





}
