package tn.esprit.usermanagement.services;


import com.google.zxing.WriterException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.entities.Shop;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface ShopServices {
    public List<Shop> ShowAllShops();
    public Shop addShopAndAffectToUser(Shop s, List<MultipartFile> files) throws Exception;
    public Shop editShop(Shop s) throws IOException;
    public Shop deleteShop(int idShop);



    public List<Shop> ShowAllShopsByUser(Integer idUser);
    public List<Product> GenerateCatalog(int idShop);
    public ResponseEntity<String> removeProductFromShop( Integer shopId,  Integer productId);
    public ResponseEntity<List<Product>> getAllProductsOfShop( Integer shopId);
    public Shop generateReportForShop(Integer shopId, LocalDateTime debut, LocalDateTime fin) throws IOException, WriterException;
}
