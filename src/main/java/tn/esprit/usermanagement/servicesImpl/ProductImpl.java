package tn.esprit.usermanagement.servicesImpl;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code93Writer;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Pictures;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.entities.Shop;
import tn.esprit.usermanagement.repositories.PicturesRepo;
import tn.esprit.usermanagement.repositories.ProductRepo;
import tn.esprit.usermanagement.repositories.ShopRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.IProductService;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductImpl implements IProductService {
    private ProductRepo productRepo;
    ShopRepo shopRepo;
    UserRepo userRepo;
    PicturesRepo picturesRepo;
    RefGenerator refGenerator;
    @Override
    public Product ajouter(Product product) {
        return productRepo.save(product);
    }

    @Override
    @Transactional
    public Product addProductToShop(Product product, Integer shopId, List<MultipartFile> files) throws Exception {
        String ref =refGenerator.generateRef();
        Product tmp = productRepo.findByReference(ref);
        while (tmp!=null) {
            ref = refGenerator.generateRef();
            tmp = productRepo.findByReference(ref);
        }
        product.setReference(ref);



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

        savedProd.setPictures(picturesList);
        productRepo.save(savedProd);



        return savedProd;
    }






    @Override
    public Product editProduct(Product product){

        return productRepo.save(product);

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
        return productRepo.findAll();
    }



    @Override
    public List<Product> SortProductByCategory(String category) {
        return productRepo.SortProductByCategory(category);
    }


    public List<Product> searchProducts(String reference, String name, Integer quantity,
                                        Float price, String description, String brand) {
        return productRepo.searchProducts(reference, name, quantity, price, description, brand);
    }

    public List<Product> getProductsByPriceRange(float minPrice, float maxPrice) {
        return productRepo.findByPriceBetween(minPrice, maxPrice);
    }



}
