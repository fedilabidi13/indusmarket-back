package tn.esprit.usermanagement.services;

import tn.esprit.usermanagement.dto.DeliveryDTO;
import tn.esprit.usermanagement.entities.Delivery;
import tn.esprit.usermanagement.entities.Orders;
import tn.esprit.usermanagement.entities.User;

import java.util.List;
import java.util.Optional;

public interface DeliveryServices {
    public List<Delivery> findAll();
    public Delivery save(Delivery delivery);
    public Optional<Delivery> findById(Integer id);

    public void deleteById(Integer id);
    public List<Delivery> rechercherDeliveryList(String dest);
    public Delivery attribuerLivreur(Integer idDelivery, Integer livreurId);

public List<User>  returnUserLivreurfromdel(Integer id);
   public List<Delivery> findDeliveriesByLivreurIdAndClientIdAndPoids( Integer livreurId, Integer clientId);
   List<DeliveryDTO> findInfoDeliveryClient(Integer idDelivery);

    public List<User> getAvailableDeliveryUsers();


    public void affecterDeliveries();
    public Delivery confirmDeliveryReceived(Integer deliveryId);
    public Boolean conditionpoids(User delivery);



}
