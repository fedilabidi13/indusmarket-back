package tn.esprit.usermanagement.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.usermanagement.repositories.ProductRepo;
import tn.esprit.usermanagement.repositories.ProductRequestRepo;
import tn.esprit.usermanagement.services.IInvoiceService;
import tn.esprit.usermanagement.services.IProductRequestService;

@RestController
@AllArgsConstructor
public class ProductRequestController {

     IProductRequestService productRequestService;
    @PostMapping("/sendEmailForClient")
    public void sendEmailForClient(@RequestParam Integer IdProduct) {
        productRequestService.sendEmailForClient(IdProduct);
    }
}
