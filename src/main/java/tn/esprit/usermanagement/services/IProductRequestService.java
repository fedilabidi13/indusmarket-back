package tn.esprit.usermanagement.services;

import tn.esprit.usermanagement.entities.ProductRequest;

import java.util.List;

public interface IProductRequestService {
     void sendEmailForClient ( Integer IdProduct );
     public List<ProductRequest> all(Integer IdProduct);

}
