package tn.esprit.usermanagement.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.services.IProductService;

import java.util.List;

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

    @PostMapping(path = "/AddProductAndAssignToShop/{ShopId}")
    public Product addProductToShop(@ModelAttribute Product p,@PathVariable("ShopId") int ShopId,@RequestParam("file") List<MultipartFile> files) throws Exception {
        return productService.addProductToShop(p,ShopId,files);


    }
    @PostMapping(path ="/editProduct" )
    public Product editProduct(@RequestBody Product product){
        return productService.editProduct(product);
    }
    @DeleteMapping(path = "/deleteProduct/{idProduct}")
    public void deleteProduct(@PathVariable("idProduct") int idProduct){
        productService.deleteProduct(idProduct);
    }
    @GetMapping(path ="/ShowAllProductsWithoutDiscount" )
    public List<Product> ShowAllProductsWithoutDiscount(){
        return productService.ShowAllProductsWithoutDiscount();
    }
    @GetMapping(path ="/ShowAllProductsWithDiscount" )
    public List<Product> ShowAllProductsWithDiscount(){
        return productService.ShowAllProductsWithDiscount();
    }
    @PostMapping(path = "/ApplicateDiscount/{Discount}/{idProd}")
    public Product ApplicateDiscount(@PathVariable("Discount") int Discount,@PathVariable("idProd") int idProd){
        return productService.ApplicateDiscount(Discount,idProd);
    }
    @GetMapping(path ="/ShowAllProducts" )
    public List<Product> ShowAllProducts(){
        return productService.ShowAllProducts();
    }
    @GetMapping(path = "/SortProductByCategory/{cat}")
    public List<Product> SortProductByCategory(@PathVariable("cat") String category){
        return productService.SortProductByCategory(category);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer quantity,
            @RequestParam(required = false) Float price,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String brand) {

        List<Product> products = productService.searchProducts(reference, name, quantity, price, description, brand);

        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(products, HttpStatus.OK);
        }
    }

    @GetMapping("/PriceBetween")
    public List<Product> getProductsByPriceRange(
            @RequestParam("min") float minPrice,
            @RequestParam("max") float maxPrice) {
        return productService.getProductsByPriceRange(minPrice, maxPrice);
    }
}
