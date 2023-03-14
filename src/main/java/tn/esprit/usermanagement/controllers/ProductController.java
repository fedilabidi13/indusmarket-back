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


    @PostMapping(path = "/add")
    public Product addProductToShop(@ModelAttribute Product p,@RequestParam int quantity,@RequestParam int ShopId,@RequestParam("file") List<MultipartFile> files) throws Exception {
        return productService.addProductToShop(p,quantity,ShopId,files);


    }
    @PostMapping(path ="/update" )
    public Product editProduct(@RequestBody Product product,@RequestParam int id){
        return productService.editProduct(product,id);
    }
    @DeleteMapping(path = "/delete")
    public void deleteProduct(@RequestParam int idProduct){
        productService.deleteProduct(idProduct);
    }
    @GetMapping(path ="/findByNoDiscount" )
    public List<Product> ShowAllProductsWithoutDiscount(){
        return productService.ShowAllProductsWithoutDiscount();
    }
    @GetMapping(path ="/findByDiscount" )
    public List<Product> ShowAllProductsWithDiscount(){
        return productService.ShowAllProductsWithDiscount();
    }
    @PostMapping(path = "/addDiscount")
    public Product ApplicateDiscount(@RequestParam int Discount,@RequestParam int idProd){
        return productService.ApplicateDiscount(Discount,idProd);
    }
    @GetMapping(path ="/findAll" )
    public List<Product> ShowAllProducts(){
        return productService.ShowAllProducts();
    }
    @GetMapping(path = "/sortByCategory")
    public List<Product> SortProductByCategory(@RequestParam String category){
        return productService.SortProductByCategory(category);
    }

    @GetMapping("/find")
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

    @GetMapping("/findByMaxAndMinPrice")
    public List<Product> getProductsByPriceRange(
            @RequestParam("min") float minPrice,
            @RequestParam("max") float maxPrice) {
        return productService.getProductsByPriceRange(minPrice, maxPrice);
    }
    @GetMapping("/compare")
    public List<String> compareProducts(@RequestParam int productId1, @RequestParam int productId2) {
        return productService.compareProductFeatures(productId1,productId2);
    }
    @PostMapping("/updateQuantity")
    public Product updateQte(@RequestParam Integer id,@RequestParam Integer qte)
    {
        return productService.updateProductQuantity(id,qte);
    }
}
