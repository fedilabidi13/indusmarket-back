package tn.esprit.usermanagement.controllers;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/Shop")
public class ShopController {

    ShopServices shopServices;

    ProductRepo productRepo;

    UserRepo userRepo;

    ShopRepo shopRepo;
    AuthenticationService authenticationService;



    @PostMapping( "/addShopAndAffectToUser")
    public Shop addShopAndAffectToUser(@ModelAttribute Shop s, @RequestParam("file") List<MultipartFile> files) throws Exception{
        Integer idUsr = authenticationService.currentlyAuthenticatedUser().getId();
        return shopServices.addShopAndAffectToUser(s, idUsr, s.getAdresse(), files);
    }


    @PostMapping("/editShop")
        public Shop editShop(@RequestBody Shop s) throws IOException {
       return shopServices.editShop(s);
    }
    @DeleteMapping( "/deleteShop/{idShop}")
    public Shop deleteShop(@PathVariable("idShop") int idShop){
        Integer idUser = authenticationService.currentlyAuthenticatedUser().getId();

        return shopServices.deleteShop(idUser,idShop);
    }
    @GetMapping( "/ShowAllShopsByUser")
    public List<Shop> ShowAllShopsByUser(){
        Integer idUser = authenticationService.currentlyAuthenticatedUser().getId();

        return shopServices.ShowAllShopsByUser(idUser);
    }
    @GetMapping("/GenerateCatalog/{idShop}")
    public List<Product> GenerateCatalog(@PathVariable ("idShop") int idShop){
        return shopServices.GenerateCatalog(idShop);
    }
    @GetMapping("/ShowAllShops" )
    public List<Shop> ShowAllShops(){return shopServices.ShowAllShops();}
     @DeleteMapping("/removeProductFromShop/{shopId}/{productId}")
     public ResponseEntity<String> removeProductFromShop(@PathVariable("shopId") int shopId, @PathVariable("productId") int productId) {
        return shopServices.removeProductFromShop(shopId,productId);
     }
    @GetMapping("/getAllProductsOfShop/{shopId}")
    public ResponseEntity<List<Product>> getAllProductsOfShop(@PathVariable int shopId) {
        return shopServices.getAllProductsOfShop(shopId);
    }

    @GetMapping("/generateReportForShop/{shopId}")
    public double generateReportForShop(@PathVariable ("shopId") Integer shopId) {
        return shopServices.generateReportForShop(shopId);
    }



    }
