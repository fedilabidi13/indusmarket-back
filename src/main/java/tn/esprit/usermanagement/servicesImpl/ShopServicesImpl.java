package tn.esprit.usermanagement.servicesImpl;


import com.cloudinary.Cloudinary;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.*;
import tn.esprit.usermanagement.repositories.*;
import tn.esprit.usermanagement.services.ShopServices;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
        private Cloudinary cloudinary;
        private MediaRepo mediaRepo;

        @Override
        public List<Shop> ShowAllShops() {
            return shopRepo.ShowAllShops();
        }

        @Override
        public Shop addShopAndAffectToUser(Shop s, List<MultipartFile> files) throws Exception {
            s.setUser(authenticationService.currentlyAuthenticatedUser());
            s.setAddress(addressRepo.save(addressService.AddAddress(s.getAdresse())));
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
            s.setMedias(mediaList);
            s.setValidated(false);
            //s.setPicturesList(picturesList);
            shopRepo.save(s);

        return s;
    }
    @Override
    public Shop editShop(Shop s) throws IOException {
        if(shopRepo.getReferenceById(s.getIdShop()).getUser().getId()== authenticationService.currentlyAuthenticatedUser().getId()){
           Address newAdresse = addressService.AddAddress(s.getAdresse());
           newAdresse.setId(shopRepo.getReferenceById(s.getIdShop()).getAddress().getId());
           s.setAddress(addressRepo.save(newAdresse));
           s.setUser(authenticationService.currentlyAuthenticatedUser());
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
        public Shop generateReportForShop(Integer shopId, LocalDateTime debut, LocalDateTime fin)  throws IOException, WriterException {
            float somme = 0;
          if ( debut.compareTo(fin)>0){
              throw new IllegalStateException("Start Date Must Be Before End Date");
          }
            Shop shop = shopRepo.getReferenceById(shopId);
            // Create a new PDF document
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            PdfWriter pdfWriter = new PdfWriter(pdfOutputStream);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);
            // Add a title to the PDF
            Paragraph title = new Paragraph(shop.getName());
            title.setTextAlignment(TextAlignment.CENTER);
            document.add(title);
            document.add(new Paragraph("\n")); // add a blank line
            document.add(new Paragraph("\n")); // add a blank line
            // Add other information to the PDF
            Paragraph userInfo = new Paragraph("Shop Information:");
            userInfo.setBold();
            userInfo.setMarginTop(20);
            document.add(userInfo);
            // Add shop name and email to the PDF
            Paragraph name = new Paragraph("Name: " + shop.getName());
            document.add(name);
            Paragraph email = new Paragraph("Email: " + shop.getMail());
            document.add(email);
            // Add order items to the PDF
            Table table = new Table(new float[]{4, 1, 2});
            table.setWidth(UnitValue.createPercentValue(100));
            table.addCell(new Cell().add(new Paragraph("Product")));
            table.addCell(new Cell().add(new Paragraph("Initial Quantity")));
            table.addCell(new Cell().add(new Paragraph("Current Quantity")));
            for (Product product: shop.getProducts())
            {
                if ( (product.getSoldAt().isAfter(debut)) && (product.getSoldAt().isBefore(fin)) )
                {
                    somme += (product.getStock().getInitialQuantity()- product.getStock().getCurrentQuantity())*product.getPrice();
                    table.addCell(new Cell().add(new Paragraph(product.getName())));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(product.getStock().getInitialQuantity()))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(product.getStock().getCurrentQuantity()))));
                }
            }
            shop.setSomme(somme);
            Paragraph Somme = new Paragraph("the Global sum : " + somme);
            document.add(Somme);
            document.add(table);
            document.add(new Paragraph("\n")); // add a blank line
            document.add(new Paragraph("\n")); // add a blank line
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy 'at' HH:mm");
            String formattedDateTime = LocalDateTime.now().format(formatter);
            // Create the paragraph with the formatted date and time
            Paragraph date = new Paragraph(formattedDateTime);
            date.setTextAlignment(TextAlignment.CENTER);
            document.add(date);
            //Add the logo to the page
            Image logo = new Image(ImageDataFactory.create("usr/src/main/resources/assets/logo.png"));
            logo.setWidth(75);
            logo.setHeight(50);
            logo.setFixedPosition(document.getLeftMargin()-20 , document.getPageEffectiveArea(document.getPdfDocument().getDefaultPageSize()).getTop() - 20);
            document.add(logo);
            // Close the PDF document
            document.close();
            byte[] pdfData = pdfOutputStream.toByteArray();
            // Save the PDF to a file
            String fileName = shop.getName() + ".pdf";
            String filePath = "usr/src/main/resources/assets/" + fileName;
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(pdfData);
            }
            return shopRepo.save(shop);
        }

    }
