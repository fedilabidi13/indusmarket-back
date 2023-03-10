package tn.esprit.usermanagement.servicesImpl;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code93Writer;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Pictures;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.entities.Shop;
import tn.esprit.usermanagement.entities.Stock;
import tn.esprit.usermanagement.repositories.*;
import tn.esprit.usermanagement.services.IProductService;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.*;

@Service
@AllArgsConstructor
public class ProductImpl implements IProductService {
    private ProductRepo productRepo;
    ShopRepo shopRepo;
    UserRepo userRepo;
    StockRepo stockRepo;
    PicturesRepo picturesRepo;
    RefGenerator refGenerator;
    EmailService emailService;
    static Integer oneTimeEmail =0;
    static int numberOfMostSold=4;
    @Override
    public Product ajouter(Product product) {
        return productRepo.save(product);
    }

    @Override
    @Transactional
    public Product addProductToShop(Product product,Integer quantity, Integer shopId, List<MultipartFile> files) throws Exception {
        String ref =refGenerator.generateRef();
        Product tmp = productRepo.findByReference(ref);
        while (tmp!=null) {
            ref = refGenerator.generateRef();
            tmp = productRepo.findByReference(ref);
        }
        product.setReference(ref);
        Stock s = new Stock();
        s.setInitialQuantity(quantity);
        s.setCurrentQuantity(quantity);
        stockRepo.save(s);
        // Vérification que le shop existe dans la base de données
        Shop shop = shopRepo.findById(shopId).orElseThrow(() -> new Exception("Shop with id " + shopId + " not found."));
        // Génération du code-barres
        String barcodeText = product.getReference();
        int width = 500;
        int height = 250;
        Code93Writer qrCodeWriter = new Code93Writer();
        BitMatrix bitMatrix = qrCodeWriter.encode(barcodeText, BarcodeFormat.CODE_93, width, height);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        // Enregistrement du produit dans la base de données
        product.setShop(shop);
        product.setBarcodeImage(pngData);
        // Enregistrement de l'image du code-barres
        FileOutputStream fos = new FileOutputStream("src/main/resources/assets/"+product.getReference()+".png");
        fos.write(pngData);
        fos.flush();
        fos.close();
        // Retour de l'objet Product avec le code-barres et autres attributs
        Product savedProd = productRepo.save(product);
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
        savedProd.setStatus("In Stock");
        savedProd.setPictures(picturesList);
        savedProd.setValidated(false);
        savedProd.setStock(s);
        productRepo.save(savedProd);
        return savedProd;
    }

    @Override
    public Product editProduct(Product product, int id){
        Product p = productRepo.findById(id).orElse(null);
        if(product.getPictures() != null)
            p.setPictures(product.getPictures());
        if(product.getDiscount() !=0)
            p.setDiscount(product.getDiscount());
        if(product.getPrice() != 0)
            p.setPrice(product.getPrice());
        if(product.getBrand() != null)
            p.setBrand(product.getBrand());
        if(product.getName()!=null)
            p.setName(product.getName());
        if(product.getCategory()!=null)
            p.setCategory(product.getCategory());
        if(product.getDescription()!=null)
            p.setDescription(product.getDescription());
        return productRepo.save(p);

    }

    @Override
    public void deleteProduct( int idProduct) {
        Product p = productRepo.findById(idProduct).get();
        productRepo.delete(p);
    }

    @Override
    public List<Product> ShowAllProductsWithoutDiscount() {
        return productRepo.ShowAllProductsWithoutDiscount();
    }

    @Override
    public List<Product> ShowAllProductsWithDiscount() {
        return productRepo.ShowAllProductsWithDiscount();
    }

    @Override
    public Product ApplicateDiscount(int Discount,int idProd) {
        Product p = productRepo.findById(idProd).get();
        p.setDiscount(Discount);
        float x= p.getPrice()*(100-Discount)/100;
        p.setPriceAfterDiscount(x);

        return productRepo.save(p);
    }

    @Override
    public List<Product> ShowAllProducts() {
        List<Product> resultWithMostSold =mostSoldPruducts(productRepo.findAll());
        return resultWithMostSold;
    }

    @Override
    public List<Product> SortProductByCategory(String category) {
        return productRepo.SortProductByCategory(category);
    }


    public List<Product> searchProducts(String reference, String name, Integer quantity,
                                        Float price, String description, String brand) {
        List<Product> resultWithMostSold =mostSoldPruducts(productRepo.searchProducts(reference, name, quantity, price, description, brand));
        return resultWithMostSold;
    }

    public List<Product> getProductsByPriceRange(float minPrice, float maxPrice) {
        return productRepo.findByPriceBetween(minPrice, maxPrice);
    }
        @Scheduled(cron = "0 */5 * * * *")
        public void checkProductQuantity() {
            List<Product> products = productRepo.findAll();
            for (Product product : products) {
                if (product.getStock().getCurrentQuantity() <= 0.3 * product.getStock().getInitialQuantity() && oneTimeEmail ==0) {
                    emailService.send(product.getShop().getUser().getEmail(),product.toString());
                    oneTimeEmail = 1;
                } else if (product.getStock().getCurrentQuantity() > 0.3 * product.getStock().getInitialQuantity()) {
                    oneTimeEmail = 0;
                }
            }
        }

        public Product updateProductQuantity(Product p ,int quantity){
            int newQuantity =p.getStock().getCurrentQuantity() + quantity;
            p.getStock().setInitialQuantity(newQuantity);
            p.getStock().setCurrentQuantity(newQuantity);
            productRepo.save(p);
            return p;
        }
        public List<Product> mostSoldPruducts(List<Product> listToGetMostSold){
            List<Product> holder = new ArrayList<>(listToGetMostSold);
            Comparator<Product> sold = Comparator.comparingInt(p -> p.getStock().howManySold());
            Collections.sort(listToGetMostSold, sold);
            listToGetMostSold = listToGetMostSold.subList(0,Math.min(numberOfMostSold,listToGetMostSold.size()));
            Iterator<Product> iterator = holder.iterator();
            while (iterator.hasNext()) {
                Product p = iterator.next();
                if (listToGetMostSold.contains(p)) {
                    iterator.remove();
                }
            }
            Collections.reverse(holder);
            holder.addAll(listToGetMostSold);
            Collections.reverse(holder);
            return holder;
        }

}
