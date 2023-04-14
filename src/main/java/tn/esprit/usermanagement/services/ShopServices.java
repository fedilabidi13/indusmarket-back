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
     List<Shop> ShowAllShops();
     Shop addShopAndAffectToUser(Shop s, List<MultipartFile> files) throws Exception;
     Shop editShop(Shop s, List<MultipartFile> files) throws IOException;
     Shop deleteShop(int idShop);
     List<Shop> ShowAllShopsByUser();
     List<Product> GenerateCatalog(int idShop);
     ResponseEntity<String> removeProductFromShop( Integer shopId,  Integer productId);
     ResponseEntity<List<Product>> getAllProductsOfShop( Integer shopId);
     Shop generateReportForShop(Integer shopId, LocalDateTime debut, LocalDateTime fin) throws IOException, WriterException;
}
