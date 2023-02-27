package tn.esprit.usermanagement.servicesImpl;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.repositories.ProductRepo;
import tn.esprit.usermanagement.services.IProductService;

@Service
@AllArgsConstructor
public class ProductImpl implements IProductService {
    private ProductRepo productRepo;
    @Override
    public Product ajouter(Product product) {
        return productRepo.save(product);
    }
}
