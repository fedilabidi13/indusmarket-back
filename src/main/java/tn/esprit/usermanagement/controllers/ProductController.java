package tn.esprit.usermanagement.controllers;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.services.IProductService;

@RestController
@RequestMapping("/product")
@AllArgsConstructor

public class ProductController {
    private IProductService productService;
    @PostMapping("/add")
    @ResponseBody
    public Product dfghjk(@RequestBody Product product)
    {
        return productService.ajouter(product);
    }
}
