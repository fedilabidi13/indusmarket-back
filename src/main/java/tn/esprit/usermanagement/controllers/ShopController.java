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

import java.util.List;
@RestController
@AllArgsConstructor
@RequestMapping("/Shop")
public class ShopController {

    ShopServices shopServices;

    ProductRepo productRepo;

    UserRepo userRepo;

    ShopRepo shopRepo;



    @PostMapping( "/addShopAndAffectToUser/{idUsr}")
    public Shop addShopAndAffectToUser(@ModelAttribute Shop s, @PathVariable("idUsr") int idUsr, @RequestParam("file") List<MultipartFile> files) throws Exception{

        return shopServices.addShopAndAffectToUser(s, idUsr, s.getAdresse(), files);
    }


    @PostMapping("/editShop")
        public Shop editShop(@RequestBody Shop s){
       return shopServices.editShop(s);
    }
    @DeleteMapping( "/deleteShop/{idUser}/{idShop}")
    public Shop deleteShop(@PathVariable("idUser") int idUser,@PathVariable("idShop") int idShop){
        return shopServices.deleteShop(idUser,idShop);
    }
    @GetMapping( "/ShowAllShopsByUser/{idUser}")
    public List<Shop> ShowAllShopsByUser(@PathVariable("idUser") int idUser){
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





}
