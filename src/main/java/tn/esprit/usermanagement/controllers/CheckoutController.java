package tn.esprit.usermanagement.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tn.esprit.usermanagement.entities.Event;
import tn.esprit.usermanagement.repositories.EventRepo;
import tn.esprit.usermanagement.repositories.OrderRepo;
import tn.esprit.usermanagement.services.IOrderService;

@Controller

public class CheckoutController {
    @Autowired
    private IOrderService orderService;
    @Autowired
    EventRepo eventRepo;
    @Autowired
    OrderRepo orderRepo;
    @Value("${STRIPE_PUBLIC_KEY}")
    public String stripePublicKey;
    @GetMapping("/checkout")
    public String checkout (Model model, @RequestParam(required = true) String orderId)
    {
        Integer amountint = Integer.valueOf((int) (orderService.calculerAmount(Integer.valueOf(orderId))*100));
        model.addAttribute("amount",amountint);
        model.addAttribute("stripePublicKey",stripePublicKey);
        model.addAttribute("currency","USD");
        model.addAttribute("orderId",orderId);
        return "checkout";
    }
    @GetMapping("/checkoutEvent")
    public String checkoutEvent (Model model, @RequestParam(required = true) String EventId)
    {
        Event event = eventRepo.findById(Integer.valueOf(EventId)).get();

        Long amountint = (event.getPrice());
        model.addAttribute("amount",amountint);
        model.addAttribute("stripePublicKey",stripePublicKey);
        model.addAttribute("currency","USD");
        model.addAttribute("EventId",EventId);
        return "checkout";
    }

}
