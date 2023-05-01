package tn.esprit.usermanagement.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.enumerations.Category;
import tn.esprit.usermanagement.repositories.ProductRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.repositories.UserSearchHistoryRepo;
import tn.esprit.usermanagement.services.IProductService;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;
import tn.esprit.usermanagement.servicesImpl.ProductImpl;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/product")
@AllArgsConstructor

public class ProductController {
    private IProductService productService;
    private ProductImpl productImpl;
    private final ProductRepo productRepo;


    @PostMapping(path = "/add")
    public Product addProductToShop(@ModelAttribute Product p,@RequestParam("quantity") int quantity,@RequestParam("shopId") int ShopId,@RequestParam("file") List<MultipartFile> files) throws Exception {
        return productService.addProductToShop(p,quantity,ShopId,files);


    }
    @PostMapping(path ="/update" )
    public Product editProduct(@ModelAttribute Product product,@RequestParam List<MultipartFile> file) throws Exception {
        return productService.editProduct(product,file);
    }
    @DeleteMapping(path = "/delete/{idProduct}")
    public void deleteProduct(@PathVariable("idProduct") int idProduct){
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
    public Product ApplicateDiscount(@RequestParam("Discount") int Discount,@RequestParam("idProd") int idProd){
        return productService.ApplicateDiscount(Discount,idProd);
    }
    @GetMapping(path ="/findAll" )
    public List<Product> ShowAllProducts(){
        return productService.ShowAllProducts();
    }
    @GetMapping(path = "/sortByCategory")
    public List<Product> SortProductByCategory(@RequestParam("category") String category){

        return productService.findByCategory(Category.valueOf(category));
    }

    @GetMapping("/find")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer quantity,
            @RequestParam(required = false) Float price,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String category)
    {
            List<Product> products = productService.searchProducts(reference, name, quantity, price, description, brand,category);

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
    public List<String> compareProducts(@RequestParam("productId1") int productId1, @RequestParam("productId2") int productId2) {
        return productService.compareProductFeatures(productId1,productId2);
    }
    @PostMapping("/updateQuantity")
    public String updateQte(@RequestParam("id") Integer id,@RequestParam("qte") Integer qte)
    {
        return productService.updateProductQuantity(id,qte);
    }

    @GetMapping("/showProductsToSpeceficUser")
    public List<Product> showProductsToSpeceficUser(){
        return productImpl.showProductsToSpeceficUser();
    }
    @GetMapping("/mostSoldFirst")
    public List<Product>  listToGetMostSold(){
        return productImpl.mostSoldPruducts();
    }

    @GetMapping("/ShowAllProductsForUser/{id}")
    public List<Product> ShowAllProductsForUser(@PathVariable("id") Long id){
        return productService.ShowAllProductsForUser(id);
    }
    @GetMapping("/findByid")
    public Product getAproduct(@RequestParam Integer id)
    {
        return productRepo.getReferenceById(id);
    }
}
