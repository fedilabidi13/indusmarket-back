package tn.esprit.usermanagement.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.ProductRequest;
import tn.esprit.usermanagement.repositories.ProductRepo;
import tn.esprit.usermanagement.repositories.ProductRequestRepo;
import tn.esprit.usermanagement.services.IInvoiceService;
import tn.esprit.usermanagement.services.IProductRequestService;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")

public class ProductRequestController {

     IProductRequestService productRequestService;
    @PostMapping("/sendEmailForClient")
    public void sendEmailForClient(@RequestParam Integer IdProduct) {
        productRequestService.sendEmailForClient(IdProduct);
    }


    @GetMapping("/all")
     public List<ProductRequest> all(@RequestParam Integer IdProduct)
     {
       return productRequestService.all(IdProduct);
      }

}
