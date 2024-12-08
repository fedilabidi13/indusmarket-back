package tn.esprit.usermanagement.controllers;

import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.dto.ChargeRequest;
import tn.esprit.usermanagement.entities.Orders;
import tn.esprit.usermanagement.repositories.OrderRepo;
import tn.esprit.usermanagement.repositories.ShoppingCartRepo;
import tn.esprit.usermanagement.services.IOrderService;
import tn.esprit.usermanagement.servicesImpl.StripeService;



@Controller
@CrossOrigin(origins = "*")



public class ChargeController {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private IOrderService orderService;


    @Autowired
    private StripeService paymentsService;

    @Autowired
    private ShoppingCartRepo shoppingCartRepo;

    @PostMapping("/charge")

    public String charge(ChargeRequest chargeRequest, Model model)
            throws StripeException {
        chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(ChargeRequest.Currency.EUR);
        Charge charge = paymentsService.charge(chargeRequest);
        model.addAttribute("id", charge.getId());
        model.addAttribute("status", charge.getStatus());
        model.addAttribute("chargeId", charge.getId());
        model.addAttribute("balance_transaction", charge.getBalanceTransaction());
        return "result";

    }

    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return "result";
    }

    @Autowired
    private StripeService stripeService;

    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;

//    @PostMapping("/charge")
//   public String charge(ChargeRequest chargeRequest, Model model)
//            throws StripeException {
//        chargeRequest.setDescription("Example charge");
//        chargeRequest.setCurrency(ChargeRequest.Currency.EUR);
//        Charge charge = paymentsService.charge(chargeRequest);
//        model.addAttribute("id", charge.getId());
//        model.addAttribute("status", charge.getStatus());
//        model.addAttribute("chargeId", charge.getId());
//        model.addAttribute("balance_transaction", charge.getBalanceTransaction());
//        return "result";
//
//    }

    @ExceptionHandler(StripeException.class)
    public ResponseEntity<String> handleError(StripeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }


}