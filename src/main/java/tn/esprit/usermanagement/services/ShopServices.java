package tn.esprit.usermanagement.services;


import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.entities.Shop;

import java.io.IOException;
import java.util.List;

public interface ShopServices {
    public List<Shop> ShowAllShops();
    public Shop addShopAndAffectToUser(Shop s, int idUsr,String address, List<MultipartFile> files) throws Exception;
    public Shop editShop(Shop s) throws IOException;
    public Shop deleteShop(int idUser, int idShop);
    public List<Shop> ShowAllShopsByUser(int idUser);
    public List<Product> GenerateCatalog(int idShop);
    public ResponseEntity<String> removeProductFromShop( Integer shopId,  Integer productId);
    public ResponseEntity<List<Product>> getAllProductsOfShop( Integer shopId);
    public double generateReportForShop(Integer shopId) ;


    }
