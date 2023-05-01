package tn.esprit.usermanagement.services;


import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.enumerations.Category;

import java.util.List;

public interface IProductService {
    Product ajouter(Product product);
     Product addProductToShop(Product product,Integer quantity, Integer shopId, List<MultipartFile> files) throws Exception;
     Product editProduct(Product product, List<MultipartFile> files)throws Exception;
     void deleteProduct( int idProduct);
     List<Product> ShowAllProductsWithoutDiscount();
     List<Product> ShowAllProductsWithDiscount();
     Product ApplicateDiscount(int Discount,int idProd);
     List<Product> ShowAllProducts();
     List<Product> findByCategory(Category category);
     List<Product> searchProducts(String reference, String name, Integer quantity,Float price, String description, String brand,String category);
     List<Product> getProductsByPriceRange(float minPrice, float maxPrice);
     List<Product> mostSoldPruducts();
     List<String> compareProductFeatures(int product1id, int product2id);
     String updateProductQuantity(int id ,int quantity);
     List<Product> showProductsToSpeceficUser();
    List<Product> ShowAllProductsForUser(Long id);
    }
