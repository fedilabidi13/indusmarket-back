package tn.esprit.usermanagement.servicesImpl;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Pictures;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.entities.Shop;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.PicturesRepo;
import tn.esprit.usermanagement.repositories.ProductRepo;
import tn.esprit.usermanagement.repositories.ShopRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.ShopServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

    @Service
    @AllArgsConstructor
public class ShopServicesImpl implements ShopServices {
    ShopRepo shopRepo;
    UserRepo userRepo;
    PicturesRepo picturesRepo;
    ProductRepo productRepo;
    AddressService addressService;

        @Override
        public List<Shop> ShowAllShops() {
            return shopRepo.ShowAllShops();
        }

        @Override
    public Shop addShopAndAffectToUser(Shop s, int idUsr,String address, List<MultipartFile> files) throws Exception {
        User u = userRepo.findById(idUsr).get();


            Shop shop = shopRepo.save(s);
            shop.setUser(u);
            shop.setAddress(addressService.AddAddress(address));
            List<Pictures> picturesList = new ArrayList<>();
            for (MultipartFile file : files) {
                Pictures picture = new Pictures();
                byte[] data = file.getBytes();
                if (data.length > 500) { // check if the file is too large
                    data = Arrays.copyOfRange(data, 0, 500); // truncate the data
                }
                picture.setData(data);
                picturesList.add(picture);
            }
            picturesRepo.saveAll(picturesList);

            shop.setPicturesList(picturesList);
            shopRepo.save(shop);

        return shop;
    }




    @Override
    public Shop editShop(Shop s)  {
        return shopRepo.save(s);
    }

    @Override
    public Shop deleteShop(int idUser, int idShop) {
        User usr = userRepo.findById(idUser).get();
        Shop s = shopRepo.findById(idShop).orElse(null);
        if(s==null) {
            throw new IllegalStateException("This shop does not exist");
        }
        if(usr.getShops().contains(s)==false) {
            throw new IllegalStateException("You aren't the owner of this shop");
        }
        usr.getShops().remove(s);
        shopRepo.delete(s);
        return s ;

    }

        @Override
        public List<Shop> ShowAllShopsByUser(int idUser) {
            return shopRepo.ShowAllShops(idUser);
        }
        @Override
        public List<Product> GenerateCatalog(int idShop) {
            return shopRepo.GenerateCatalog(idShop);
        }

        @Override
        public ResponseEntity<String> removeProductFromShop(Integer shopId, Integer productId) {
            Shop shop = shopRepo.findById(shopId)
                    .orElseThrow(() -> new IllegalStateException("Shop not found with id " + shopId));
            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new IllegalStateException("Product not found with id " + productId));

            if (!shop.getProducts().contains(product)) {
                return ResponseEntity.badRequest().body("Product " + productId + " is not associated with Shop " + shopId);
            }

            shop.getProducts().remove(product);
            shopRepo.save(shop);
            return ResponseEntity.ok("Product " + productId + " removed from Shop " + shopId);
        }

        @Override
        public ResponseEntity<List<Product>> getAllProductsOfShop(Integer shopId) {

                Shop shop = shopRepo.findById(shopId)
                        .orElseThrow(() -> new IllegalStateException("Shop not found with id " + shopId));
                List<Product> products = shop.getProducts();
                return ResponseEntity.ok(products);
        }


    }
