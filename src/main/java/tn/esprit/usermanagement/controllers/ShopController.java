package tn.esprit.usermanagement.controllers;


import lombok.AllArgsConstructor;
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
import java.util.List;
@RestController
@AllArgsConstructor
@RequestMapping("/shop")
public class ShopController {

    ShopServices shopServices;

    ProductRepo productRepo;

    UserRepo userRepo;

    ShopRepo shopRepo;
    AuthenticationService authenticationService;




    @PostMapping( "/add")
    public Shop addShopAndAffectToUser(@ModelAttribute Shop s, @RequestParam("file") List<MultipartFile> files) throws Exception{
        Integer idUsr = authenticationService.currentlyAuthenticatedUser().getId();
        return shopServices.addShopAndAffectToUser(s, files);
    }


    @PostMapping("/update")
        public Shop editShop(@ModelAttribute Shop s) throws IOException {
      return shopServices.editShop(s);}




    @DeleteMapping( "/delete")
    public Shop deleteShop(@RequestParam int idShop){

        return shopServices.deleteShop(idShop);
    }
    @GetMapping( "/findByUser")
    public List<Shop> ShowAllShopsByUser(){
        Integer idUser = authenticationService.currentlyAuthenticatedUser().getId();
        return shopServices.ShowAllShopsByUser(idUser);
    }
    @GetMapping("/createCatalog")
    public List<Product> GenerateCatalog(@RequestParam int idShop){
        return shopServices.GenerateCatalog(idShop);
    }
    @GetMapping("/findAll" )
    public List<Shop> ShowAllShops(){return shopServices.ShowAllShops();}
     @DeleteMapping("/removeProduct")
     public ResponseEntity<String> removeProductFromShop(@RequestParam int shopId, @RequestParam int productId) {
        return shopServices.removeProductFromShop(shopId,productId);
     }
    @GetMapping("/findAllProducts")
    public ResponseEntity<List<Product>> getAllProductsOfShop(@RequestParam int shopId) {
        return shopServices.getAllProductsOfShop(shopId);
    }
/*
    @GetMapping("/createReport")
    public double generateReportForShop(@RequestParam Integer shopId, @RequestParam) {
        return shopServices.generateReportForShop(shopId);
    }

*/


}
