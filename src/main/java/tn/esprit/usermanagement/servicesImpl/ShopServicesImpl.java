package tn.esprit.usermanagement.servicesImpl;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.*;
import tn.esprit.usermanagement.repositories.*;
import tn.esprit.usermanagement.services.ShopServices;

import java.io.IOException;
import java.time.LocalDateTime;
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
    AuthenticationService authenticationService;
        private final AddressRepo addressRepo;
        private final ShoppingCartRepo shoppingCartRepo;

        @Override
        public List<Shop> ShowAllShops() {
            return shopRepo.ShowAllShops();
        }

        @Override
        public Shop addShopAndAffectToUser(Shop s, List<MultipartFile> files) throws Exception {
            s.setUser(authenticationService.currentlyAuthenticatedUser());
            s.setAddress(addressRepo.save(addressService.AddAddress(s.getAdresse())));
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
            s.setValidated(false);
            s.setPicturesList(picturesList);
            shopRepo.save(s);

        return s;
    }




    @Override
    public Shop editShop(Shop s) throws IOException {
        if(shopRepo.getReferenceById(s.getIdShop()).getUser().getId()== authenticationService.currentlyAuthenticatedUser().getId()){
           Address newAdresse = addressService.AddAddress(s.getAdresse());
           newAdresse.setId(shopRepo.getReferenceById(s.getIdShop()).getAddress().getId());
           s.setAddress(addressRepo.save(newAdresse));
        return shopRepo.save(s);}
        else{
            throw new IllegalStateException("You aren't the owner of this shop");}

    }

    @Override
    public Shop deleteShop(int idShop) {
      Integer idUser = authenticationService.currentlyAuthenticatedUser().getId();
        Shop s = shopRepo.findById(idShop).get();
        if(s==null) {
            throw new IllegalStateException("This shop does not exist");
        }
        if(idUser!=s.getUser().getId()) {
            throw new IllegalStateException("You aren't the owner of this shop");
        }
        shopRepo.delete(s);
        return s ;
    }

        @Override
        public List<Shop> ShowAllShopsByUser(Integer idUser) {
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
        public Shop generateReportForShop(Integer shopId, LocalDateTime debut, LocalDateTime fin) {
            float somme = 0;
          if ( debut.compareTo(fin)>0){
              throw new IllegalStateException("Start Date Must Be Before End Date");
          }
            Shop shop = shopRepo.getReferenceById(shopId);
            for (Product product: shop.getProducts())
            {
                if ( (product.getSoldAt().isAfter(debut)) && (product.getSoldAt().isBefore(fin)) )
                {
                    somme += (product.getStock().getInitialQuantity()- product.getStock().getCurrentQuantity())*product.getPrice();
                }
            }
            shop.setSomme(somme);
            return shopRepo.save(shop);

        }

    }
