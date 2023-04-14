package tn.esprit.usermanagement.controllers;


import com.google.zxing.WriterException;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.entities.Shop;
import tn.esprit.usermanagement.repositories.ProductRepo;
import tn.esprit.usermanagement.repositories.ShopRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.ShopServices;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
@RequestMapping("/shop")
public class ShopController {

    private ShopServices shopServices;

    @PostMapping( "/add")
    public Shop addShopAndAffectToUser(@ModelAttribute Shop s, @RequestParam("file") List<MultipartFile> files) throws Exception{
        return shopServices.addShopAndAffectToUser(s, files);
    }


    @PostMapping("/update")
        public Shop editShop(@ModelAttribute Shop s,@RequestParam List<MultipartFile> file) throws IOException {
      return shopServices.editShop(s,file);}




    @DeleteMapping( "/delete")
    public Shop deleteShop(@RequestParam int idShop){

        return shopServices.deleteShop(idShop);
    }
    @GetMapping( "/findByUser")
    public List<Shop> ShowAllShopsByUser(){
        return shopServices.ShowAllShopsByUser();
    }
    @GetMapping("/createCatalog")
    public List<Product> GenerateCatalog(@RequestParam("idShop") int idShop){
        return shopServices.GenerateCatalog(idShop);
    }
    @GetMapping("/findAll" )
    public List<Shop> ShowAllShops(){return shopServices.ShowAllShops();}
     @DeleteMapping("/removeProduct")
     public ResponseEntity<String> removeProductFromShop(@RequestParam("shopId") int shopId, @RequestParam("productId") int productId) {
        return shopServices.removeProductFromShop(shopId,productId);
     }
    @GetMapping("/findAllProducts/{shopId}")
    public ResponseEntity<List<Product>> getAllProductsOfShop(@PathVariable("shopId") int shopId) {
        return shopServices.getAllProductsOfShop(shopId);
    }

    @GetMapping("/createReport")
    public Shop generateReportForShop(
            @RequestParam("shopId") Integer shopId,
            @RequestParam("deb") @DateTimeFormat(pattern = "yyyy-MM-dd ") LocalDateTime debut,
            @RequestParam("fin") @DateTimeFormat(pattern = "yyyy-MM-dd ") LocalDateTime fin
    ) throws IOException, WriterException {
        return shopServices.generateReportForShop(shopId,debut,fin);
    }




}
