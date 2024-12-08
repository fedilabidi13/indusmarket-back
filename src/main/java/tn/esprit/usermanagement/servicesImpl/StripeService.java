package tn.esprit.usermanagement.servicesImpl;

import com.paypal.api.payments.PaymentInstruction;
import com.stripe.model.PaymentIntent;
import tn.esprit.usermanagement.dto.ChargeRequest;
import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StripeService {

    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

//        chargeParams.put("source", chargeRequest.getStripeToken());


    public PaymentIntent paymentIntent(ChargeRequest paymentIntentDto) throws StripeException {
        Stripe.apiKey = secretKey;
        List<String> paymentMethodTypes = new ArrayList();
        paymentMethodTypes.add("card");
        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentIntentDto.getAmount());
        params.put("currency", paymentIntentDto.getCurrency());
        params.put("description", paymentIntentDto.getDescription());
        params.put("payment_method_types", paymentMethodTypes);
        return PaymentIntent.create(params);
    }

    public PaymentIntent confirm(String id) throws StripeException {
        Stripe.apiKey = secretKey;
        PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
        Map<String, Object> params = new HashMap<>();
        params.put("payment_method", "pm_card_visa");
        paymentIntent.confirm(params);
        return paymentIntent;
    }

    public PaymentIntent cancel(String id) throws StripeException {
        Stripe.apiKey = secretKey;
        PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
        paymentIntent.cancel();
        return paymentIntent;
    }

    public Charge charge(ChargeRequest chargeRequest)
            throws AuthenticationException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", chargeRequest.getAmount());
        chargeParams.put("currency", chargeRequest.getCurrency());
        chargeParams.put("description", chargeRequest.getDescription());
        chargeParams.put("source", chargeRequest.getStripeToken());
        try {
            return Charge.create(chargeParams);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }



}