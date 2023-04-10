package tn.esprit.usermanagement.servicesImpl;


import com.cloudinary.Cloudinary;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code93Writer;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.dto.CommunMultipartFile;
import tn.esprit.usermanagement.entities.*;
import tn.esprit.usermanagement.entities.ForumEntities.Media;
import tn.esprit.usermanagement.enumerations.Category;
import tn.esprit.usermanagement.repositories.*;
import tn.esprit.usermanagement.services.IProductService;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.*;

@Service
@AllArgsConstructor
public class ProductImpl implements IProductService {
    private Cloudinary cloudinary;
    private MediaRepo mediaRepo;
    private ProductRepo productRepo;
    private AuthenticationService authenticationService;
    private ShopRepo shopRepo;
    private StockRepo stockRepo;
    private RefGenerator refGenerator;
    private EmailService emailService;
    static int numberOfMostSold = 4;
    private UserSearchHistoryRepo userSearchRepo;

    @Override
    public Product ajouter(Product product) {
        return productRepo.save(product);
    }

    @Override
    @Transactional
    public Product addProductToShop(Product product, Integer quantity, Integer shopId, List<MultipartFile> files) throws Exception {
        String ref = refGenerator.generateRef();
        Product tmp = productRepo.findByReference(ref);
        while (tmp != null) {
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
       // MultipartFile multipartFile1 = new CommunMultipartFile(
               // new ByteArrayResource(pngData), product.getReference()+".png");
        product.setShop(shop);
        //product.setBarcodeImage(pngData);
        Media media1 = new Media();
        String url1 = cloudinary.uploader()
                .upload(pngData,
                        Map.of("public_id", UUID.randomUUID().toString()))
                .get("url")
                .toString();
        media1.setImagenUrl(url1);

        media1.setName(product.getReference()+".png");
        product.setBarcodeImage(mediaRepo.save(media1));
        // Enregistrement de l'image du code-barres
        FileOutputStream fos = new FileOutputStream("src/main/resources/assets/" + product.getReference() + ".png");
        fos.write(pngData);
        fos.flush();
        fos.close();
        // Retour de l'objet Product avec le code-barres et autres attributs
        Product savedProd = productRepo.save(product);
        List<Media> mediaList = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            Media media = new Media();
            String url = cloudinary.uploader()
                    .upload(multipartFile.getBytes(),
                            Map.of("public_id", UUID.randomUUID().toString()))
                    .get("url")
                    .toString();
            media.setImagenUrl(url);
            media.setName(multipartFile.getName());
            mediaList.add(media);
        }
        mediaRepo.saveAll(mediaList);
        savedProd.setMedias(mediaList);
        savedProd.setStatus("In Stock");
        savedProd.setValidated(false);
        savedProd.setStock(s);
        savedProd.setOneTimeEmail(true);
        productRepo.save(savedProd);
        return savedProd;
    }

    @Override
    public Product editProduct(Product product,List<MultipartFile> files)throws Exception {
        Product p = productRepo.findById(product.getIdProduct()).get();
        if (files != null){
            List<Media> mediaList = new ArrayList<>();
            for (MultipartFile multipartFile : files) {
                Media media = new Media();
                String url = cloudinary.uploader()
                        .upload(multipartFile.getBytes(),
                                Map.of("public_id", UUID.randomUUID().toString()))
                        .get("url")
                        .toString();
                media.setImagenUrl(url);
                media.setName(multipartFile.getName());
                mediaList.add(media);
            }
            mediaRepo.saveAll(mediaList);
            p.setMedias(mediaList);
        }
        if (product.getDiscount() != null)
            p.setDiscount(product.getDiscount());
        if (product.getPrice() != 0)
            p.setPrice(product.getPrice());
        if (product.getBrand() != null)
            p.setBrand(product.getBrand());
        if (product.getName() != null)
            p.setName(product.getName());
        if (product.getCategory() != null)
            p.setCategory(product.getCategory());
        if (product.getDescription() != null)
            p.setDescription(product.getDescription());
        p.setOneTimeEmail(true);
        return productRepo.save(p);

    }

    @Override
    public void deleteProduct(int idProduct) {
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
    public Product ApplicateDiscount(int Discount, int idProd) {
        Product p = productRepo.findById(idProd).get();
        p.setDiscount(Discount);
        float x = p.getPrice() * (100 - Discount) / 100;
        p.setPriceAfterDiscount(x);

        return productRepo.save(p);
    }

    @Override
    public List<Product> ShowAllProducts() {
        List<Product> productList = productRepo.findAll();
        return productList;
    }

    @Override
    public List<Product> findByCategory(Category category) {
        return productRepo.findByCategory(category);
    }


    public List<Product> searchProducts(String reference, String name, Integer quantity,
                                        Float price, String description, String brand, String category) {
        User user = authenticationService.currentlyAuthenticatedUser();

        List<String> keyWord = new ArrayList<>();
        if (reference != null) {
            keyWord.add(reference);
        }
        if (name != null) {
            keyWord.add(name);
        }
        if (quantity != null) {
            keyWord.add(String.valueOf(quantity));
        }
        if (price != null) {
            keyWord.add(String.valueOf(price));
        }
        if (description != null) {
            keyWord.add(description);
        }
        if (brand != null) {
            keyWord.add(brand);
        }
        if (category != null) {
            keyWord.add(category);
        }
        UserSearchHistory usr = new UserSearchHistory();
        usr.setUser(user);
        usr.setSearch(keyWord);
        userSearchRepo.save(usr);
        List<Product> productList = productRepo.searchProducts(reference, name, quantity, price, description, brand, category);
        return productList;
    }

    public List<Product> getProductsByPriceRange(float minPrice, float maxPrice) {
        return productRepo.findByPriceBetween(minPrice, maxPrice);
    }

    @Scheduled(cron = "*/5 * * * * *")

    public void checkProductQuantity() {

        List<Product> products = productRepo.findAll();
        for (Product product : products) {
            if (product.getStock().getCurrentQuantity() <= 0.3 * product.getStock().getInitialQuantity() && product.getOneTimeEmail() == true ) {
                String email = "<!DOCTYPE html>\n" +
                        "                        <html>\n" +
                        "                        <head>\n" +
                        "                       <meta charset=\"UTF-8\">\n" +
                        "                       <title>Titre de l'email</title>\n" +
                        "                        <style>\n" +
                        "                              /* Styles pour l'arrière-plan uni */\n" +
                        "                              body {\n" +
                        "                              background-color: #F5F5F5;\n" +
                        "                               margin: 0;\n" +
                        "                                padding: 0;  \n" +
                        "                        font-family: Arial, sans-serif;\n" +
                        "                           }\n" +
                        "                              /* Styles pour le conteneur principal */\n" +
                        "                              .container {\n" +
                        "                                max-width: 600px;\n" +
                        "                                margin: 0 auto;\n" +
                        "                               background-color: #FFFFFF;\n" +
                        "                                padding: 20px;\n" +
                        "                                height: 100vh;\n" +
                        "                                display: flex;\n" +
                        "                                flex-direction: column;\n" +
                        "                                justify-content: center;\n" +
                        "                              }\n" +
                        "                              /* Styles pour le logo de l'entreprise */\n" +
                        "                              .logo {\n" +
                        "                                display: block;\n" +
                        "                                margin: -20px auto 20px;\n" +
                        "                                width: 100px;\n" +
                        "                                height: auto;\n" +
                        "                              }\n" +
                        "                              /* Styles pour le corps du texte */\n" +
                        "                              .text {\n" +
                        "                                text-align: center;\n" +
                        "                              }\n" +
                        "                              /* Styles pour le bouton animé */\n" +
                        "                              .button {\n" +
                        "                                display: inline-block;\n" +
                        "                                font-size: 16px;\n" +
                        "                                font-weight: bold;\n" +
                        "                                color: #3CAEA3;\n" +
                        "                                background-color: transparent;\n" +
                        "                                border-radius: 5px;\n" +
                        "                               padding: 10px 20px;\n" +
                        "                                border: 2px solid #3CAEA3;\n" +
                        "                                text-decoration: none;\n" +
                        "                                transition: all 0.5s ease;\n" +
                        "                              }\n" +
                        "                              .button:hover {\n" +
                        "                                background-color: #3CAEA3;\n" +
                        "                                color: #FFFFFF;\n" +
                        "                              }\n" +
                        "                            </style>\n" +
                        "                          </head>\n" +
                        "                          <body>\n" +
                        "                            <div class=\"container\">\n" +
                        "                              <img src=\"https://i.ibb.co/nkrBqck/334886508-513260607680644-3515218608247778867-n.png\" alt=\"indusmarket logo\" padding-left=\"60%\" height=\"70px\" width=\"130px\">\n" +
                        "                        <br>     \n" +
                        "                         <div class=\\\"text\\\">\n" +
                        "                                <h1 style=\\\"color : #3CAEA3;\\\">Product Stock Alert!</h1>\n" +
                        "                                <p style =\"color: red;\">we noticed there a lot of demand on product:  </p>\n" +
                        "\t\t\t\t\t\t<p>\n" +
                        "<p>Reference: "+product.getReference()+"</p>\n" +
                        "<p>Name: "+product.getName()+"</p>\n" +
                        "<p>Price: "+product.getPrice()+"</p>\n" +
                        "<p>Description: "+product.getDescription()+"</p>\n" +
                        "<p>Quantity: "+product.getStock().getCurrentQuantity()+"</p>\n" +
                        "<p>Category: "+product.getCategory()+"</p>\n" +
                        "<p>Brand: "+product.getBrand()+"</p>\n" +
                        "\n" +
                        "\n" +
                        "</p>\n" ;
                for (Media media: product.getMedias())
                {
                    String ch="";
                    ch+="<img src=\"";
                    ch+=media.getImagenUrl();
                    ch+="\" height=\"40px\" width=\"90px\">    ";
                    email+=ch;

                }
                String email2="";
                email2+=
                        "                        <p style=\"color : red\">Please re-Stock it</p>\n" +
                        "                        \n" +
                        "                        </div>\n" +
                        "                           </div>\n" +
                        "                          </body>\n" +
                        "                        </html>";
                email+=email2;
                emailService.sendForProductRestock(product.getShop().getUser().getEmail(), email);
                product.setOneTimeEmail(false);
                productRepo.save(product);
            }
        }
    }

    @Override
    public String updateProductQuantity(int id, int quantity) {

        Product p = productRepo.getReferenceById(id);
        if(p.getShop().getUser()==authenticationService.currentlyAuthenticatedUser()){
            int newQuantity = p.getStock().getCurrentQuantity() + quantity;
            p.getStock().setInitialQuantity(newQuantity);
            p.setQuantity(newQuantity);
            p.setOneTimeEmail(true);
            stockRepo.save(p.getStock());
            productRepo.save(p);
            return "Product quantity updated";
        }
        if(p==null){
            return "Product not found";
        }
        return "You are not the owner of this product";
    }



    public List<Product> mostSoldPruducts() {
        List<Product> listToGetMostSold= productRepo.findAll();
        List<Product> holder = new ArrayList<>(listToGetMostSold);
        List<Product> holder1 = new ArrayList<>(listToGetMostSold);

        Comparator<Product> sold = Comparator.comparingInt(p -> p.getStock().howManySold());
        Collections.sort(listToGetMostSold, sold);
        Collections.reverse(listToGetMostSold);
        listToGetMostSold = listToGetMostSold.subList(0, Math.min(numberOfMostSold, listToGetMostSold.size()));
        Collections.reverse(listToGetMostSold);
        for (Product product:holder1){
            if (listToGetMostSold.contains(product)) {
                holder.remove(product);
            }
        }
        Collections.reverse(holder);
        holder.addAll(listToGetMostSold);
        Collections.reverse(holder);
        return holder;
    }

    public List<String> compareProductFeatures(int product1id, int product2id) {
        Product product1 = productRepo.getReferenceById(product1id);
        Product product2 = productRepo.getReferenceById(product2id);

        List<String> comparedFeatures = new ArrayList<>();
        if (product1.getPrice() < product2.getPrice()) {
            comparedFeatures.add(product1.getName() + " is cheaper than " + product2.getName());
        } else if (product1.getPrice() > product2.getPrice()) {
            comparedFeatures.add(product2.getName() + " is cheaper than " + product1.getName());
        } else {
            comparedFeatures.add("Both products have the same price.");
        }
        if ((product1.getStock().getCurrentQuantity()) - (product1.getStock().getCurrentQuantity()) < (product2.getStock().getCurrentQuantity()) - (product2.getStock().getCurrentQuantity())) {
            comparedFeatures.add(product1.getName() + " is less sold than " + product2.getName());
        } else if ((product1.getStock().getCurrentQuantity()) - (product1.getStock().getCurrentQuantity()) > (product2.getStock().getCurrentQuantity()) - (product2.getStock().getCurrentQuantity())) {
            comparedFeatures.add(product1.getName() + " is more sold than " + product2.getName());
        } else {
            comparedFeatures.add("Both products are sold equally");

        }
        return comparedFeatures;
    }

    public Product filtrageProduct(String ch) {

        List<Product> l1 =  productRepo.findAll();
        for (Product product : l1) {
            if ((product.getName()!=null)&&(ch.contains(product.getName()) == true) ) {
                return product;
            }
            if ((ch.contains(product.getReference()) == true)&& (product.getReference()!=null)) {
                return product;
            }
            if ((product.getPrice()!=0)&&(ch.contains(String.valueOf(product.getPrice())) == true) ) {
                return product;
            }
            if ((product.getPrice()!=0)&&(ch.contains(String.valueOf(product.getQuantity())) == true) ) {
                return product;
            }
            if ((product.getDescription()!=null)&&(ch.contains(product.getDescription()) == true)  ) {
                return product;
            }
            if ((product.getBrand()!=null) &&(ch.contains(product.getBrand()) == true) ){
                return product;
            }
            if ((product.getCategory()!=null)&&(ch.contains(product.getCategory().name()) == true) ){
                return product;
            }

        }

        return null;
    }
    @Override
    public List<Product> showProductsToSpeceficUser() {
        List<Product> products = new ArrayList<>();
        List<UserSearchHistory> list1=userSearchRepo.findAll();
        for(UserSearchHistory userSearchHistory:list1){
            if(userSearchHistory.getUser()==authenticationService.currentlyAuthenticatedUser()){
                for(String s1 :userSearchHistory.getSearch()){
                    Product p = filtrageProduct(s1);
                    if(p !=null){
                        products.add(p);
                    }
                }
            }
        }
       List<Product> products1 = products.stream().distinct().toList();

        return products1;
    }
}