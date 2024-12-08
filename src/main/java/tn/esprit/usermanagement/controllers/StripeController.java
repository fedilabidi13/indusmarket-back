package tn.esprit.usermanagement.controllers;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.dto.ChargeRequest;
import tn.esprit.usermanagement.servicesImpl.StripeService;

@RestController
@RequestMapping("/Stripe")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class StripeController {


    @Autowired
    StripeService stripeService;

    @PostMapping("/paymentintent")
    public ResponseEntity<String> payment(@RequestBody ChargeRequest chargeRequest) throws StripeException {
        PaymentIntent paymentIntent = stripeService.paymentIntent(chargeRequest);
        String paymentStr = paymentIntent.toJson();
        return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
    }

    @PostMapping("/confirm/{id}")
    public ResponseEntity<String> confirm(@PathVariable("id") String id) throws StripeException {
        PaymentIntent paymentIntent = stripeService.confirm(id);
        String paymentStr = paymentIntent.toJson();
        return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancel(@PathVariable("id") String id) throws StripeException {
        PaymentIntent paymentIntent = stripeService.cancel(id);
        String paymentStr = paymentIntent.toJson();
        return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
    }
}
