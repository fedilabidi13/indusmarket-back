package tn.esprit.usermanagement.servicesImpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.entities.Orders;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.entities.ProductRequest;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.ProductRepo;
import tn.esprit.usermanagement.repositories.ProductRequestRepo;
import tn.esprit.usermanagement.services.IProductRequestService;
import tn.esprit.usermanagement.services.IProductService;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductRequestImpl implements IProductRequestService {
    ProductRepo productRepository;
    ProductRequestRepo  productRequestRepo;
    EmailService emailService;

    @Override
    public void sendEmailForClient ( Integer IdProduct ){
        Product product = productRepository.getReferenceById(IdProduct);

        List<ProductRequest> requests = productRequestRepo.findAll();

        for (ProductRequest request : requests) {

            if ( request.getProduct().getIdProduct() == product.getIdProduct()) {
                User user = request.getUser();
                String userEmail = user.getEmail();
                String productName = product.getName();
                String emailContent = "Dear " + user.getFirstName()+ ",\n\n"
                        + "Thank you for your interest in " + productName + ". We have received your request and we "
                        + "want to tell you that the product is available now.\n\nBest regards,\nThe Product Team";
                emailService.sendForProductRequest(userEmail,  emailContent);
            }else {

                System.err.println("no client");
            }
        }


    }


    @Override
    public List<ProductRequest> all(Integer IdProduct) {
        Product product = productRepository.getOne(IdProduct);
        List<ProductRequest> requests = productRequestRepo.findByProduct(product);
        return  requests;
    }

}
