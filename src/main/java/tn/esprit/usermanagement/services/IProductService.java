package tn.esprit.usermanagement.services;


import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Product;

import java.util.List;

public interface IProductService {
    Product ajouter(Product product);
    public Product addProductToShop(Product product, Integer shopId, List<MultipartFile> files) throws Exception;
    public Product editProduct(Product product);
    public void deleteProduct( int idProduct);
    public List<Product> ShowAllProductsWithoutDiscount();
    public List<Product> ShowAllProductsWithDiscount();
    public Product ApplicateDiscount(int Discount,int idProd);
    public List<Product> ShowAllProducts();
    public List<Product> SortProductByCategory(String category);
    public List<Product> searchProducts(String reference, String name, Integer quantity,Float price, String description, String brand);
    public List<Product> getProductsByPriceRange(float minPrice, float maxPrice);
}
