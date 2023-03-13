package tn.esprit.usermanagement.servicesImpl;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.dto.DeliveryDTO;
import tn.esprit.usermanagement.entities.Delivery;
import tn.esprit.usermanagement.entities.Orders;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.enumerations.DeliveryStatus;
import tn.esprit.usermanagement.enumerations.EtatLivreur;
import tn.esprit.usermanagement.enumerations.OrdersStatus;
import tn.esprit.usermanagement.enumerations.Role;
import tn.esprit.usermanagement.repositories.DeliveryRepo;
import tn.esprit.usermanagement.repositories.OrderRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.DeliveryServices;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class DeliveryServiceImpl implements DeliveryServices {
    private DeliveryRepo deliveryRepository;
    private UserRepo userRepo;
    private OrderRepo ordersRepository;

    private GeoApiContext geoApiContext;


    @Override
    public List<Delivery> findAll() {
        return deliveryRepository.findAll();
    }

    @Override
    public Delivery save(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    @Override
    public Optional<Delivery> findById(Integer id) {
        return deliveryRepository.findById(id);
    }

    public void deleteById(Integer id) {
        deliveryRepository.deleteById(id);

    }

    @Override
    public List<Delivery> rechercherDeliveryList(String dest) {
        return deliveryRepository.findByDestinataire(dest);
    }

    @Override
    public Delivery attribuerLivreur(Integer idDelivery, Integer livreurId) {
        Delivery colis = deliveryRepository.findById(idDelivery).orElseThrow(() -> new RuntimeException("Colis introuvable"));
        colis.setDestinataire(new String());
        return deliveryRepository.save(colis);
    }

    @Override
    public List<User> returnUserLivreurfromdel(Integer id) {

        User User1 = deliveryRepository.ReturnClientFromCommande(1);
        User User2 = deliveryRepository.ReturnLivreurFromDeliveryId(1);

        List<User> users = new ArrayList<>();
        users.add(User1);
        users.add(User2);
        return users;
    }


    @Override
    public List<Delivery> findDeliveriesByLivreurIdAndClientIdAndPoids(Integer livreurId, Integer clientId) {
        List<Delivery> delivery = deliveryRepository.findByLivreur(userRepo.getReferenceById(livreurId));
        List<Delivery> deliveries = new ArrayList<>();
        for (Delivery delivery1 : delivery) {
            List<Orders> ordersList = delivery1.getOrders();
            for (Orders orders : ordersList) {
                if (orders.getUser().getId() == clientId) {
                    deliveries.add(delivery1);
                }
            }
        }

        return deliveries;
    }


    public List<DeliveryDTO> findInfoDeliveryClient(Integer idDelivery) {

        return null;
    }

    @Override
    public List<User> getAvailableDeliveryUsers() {
        List<User> results = userRepo.findLivreurDisponible();
        return results;
    }


    public List<Delivery> findLivreurDeliveriess(Integer idlivreur) {
        return deliveryRepository.findLivreurDeliveries(idlivreur);
    }


    public void attribuerLivraison(Delivery delivery) {
        List<User> deliveryUsers = userRepo.findDeliveryUsers();
        if (!deliveryUsers.isEmpty()) {
            User user = deliveryUsers.get(0);
            delivery.setLivreur(user);
            deliveryRepository.save(delivery);
        } else {
            throw new RuntimeException("No delivery users available.");
        }
    }
/*
    // Cette méthode sélectionne tous les utilisateurs avec un rôle "DELIVERY" et un état de livreur "Disponible", à l'exception de ceux qui sont actuellement en train de livrer une commande (c'est-à-dire ceux qui ont un enregistrement dans la table Delivery avec un état "En Cours")
    public void assignDelivery(Integer deliveryId, Integer deliveryUserId) throws Exception {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new Exception("Delivery not found"));
        User user = userRepo.findByIdAndRole(deliveryUserId, Role.DELIVERY);
        if (!user.getEtatLivreur().equals(EtatLivreur.DISPONIBLE)) {
            throw new Exception("Delivery user is not available");
        }
        delivery.setLivreur(user);
        delivery.setStatus(DeliveryStatus.PENDING);
        deliveryRepository.save(delivery);
    }*/

    /*
        public boolean assignDriverr(Integer deliveryId) {
            Delivery delivery = deliveryRepository.findById(deliveryId).orElse(null);
            Orders delivery1 = deliveryRepository.findUserFromOrders(deliveryId);
            if (delivery == null) {
                throw new RuntimeException("Delivery not found");
            }

            List<User> availableDrivers = userRepo.findLivreurDisponible();
            if (availableDrivers.isEmpty()) {
                return false;
            }

           // Tri des livreurs disponibles par distance croissante par rapport à la position du client
            GeoPoint clientLocation = new GeoPoint(delivery.getLatitude(), delivery.getLongitude());
            availableDrivers.sort(Comparator.comparingDouble(driver ->
                    graphHopper.calculateDistance(driver.getLatitude(), driver.getLongitude(), clientLocation.lat, clientLocation.lon)));

            User closestDriver = availableDrivers.get(0);
            delivery.setLivreur(closestDriver);
            delivery.setStatus(DeliveryStatus.PENDING);
            delivery.setClientDd(delivery1.getUser().getId());
            deliveryRepository.save(delivery);

            return true;
        }*/
    public boolean assignDriverr(Integer deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElse(null);
        User delivery1 = deliveryRepository.findUserFromOrders(deliveryId);
        if (delivery == null) {
            throw new RuntimeException("Delivery not found");
        }


        User availableDrivers = findNearestDelivery(delivery1);


        delivery.setLivreur(availableDrivers);
        delivery.setStatus(DeliveryStatus.PENDING);
        delivery.setClientDd(delivery1.getId());
        availableDrivers.setMaxPoids(affecterpoids(delivery));
        deliveryRepository.save(delivery);

        return true;
    }


    public void affecterDeliveries() {
        // Récupérer la liste des commandes avec le statut "aveclivraison"
        List<Orders> ordersList = ordersRepository.findByStatus();

        // Pour chaque commande, créer une livraison et l'associer au client et à la commande correspondants
        for (Orders order : ordersList) {
            Delivery delivery = new Delivery();
            delivery.setCode(UUID.randomUUID().toString()); // Générer un code aléatoire pour la livraison
            delivery.setStatus(DeliveryStatus.NEW);
            delivery.setDestinataire(order.getDilevryAdresse());
            delivery.setVille(order.getDilevryAdresse());
            delivery.setClientDd(order.getUser().getId()); // Associer le client correspondant à la commande
            delivery.setOrders(Arrays.asList(order)); // Associer la commande correspondante à la livraison
            deliveryRepository.save(delivery);

        }
    }


    public Delivery confirmDeliveryReceived(Integer deliveryId) {
        Delivery delivery = deliveryRepository.findDeliveryBySonId(deliveryId);
        User user = delivery.getLivreur();

        Integer idClien = delivery.getClientDd();
        User user1 = userRepo.findById2(idClien);

        if (delivery == null) {
            throw new RuntimeException("La livraison avec l'ID " + deliveryId + " n'existe pas");
        }

        double distance = distance(user.getLatitude(), user.getLatitude(), user1.getLatitude(), user1.getLongitude());
        if (distance < 100) {
            delivery.setStatus(DeliveryStatus.DELIVERED);
            Delivery updatedDelivery = deliveryRepository.save(delivery);
        }

        // Confirmer la livraison comme reçue


        return delivery;
    }

    @Override
    public Boolean conditionpoids(User delivery) {

        if (delivery.getMaxPoids()>100){
            delivery.setEtatLivreur(EtatLivreur.OCUPPE);
            return true;
        }

        return false;
    }




    public Integer affecterpoids(Delivery delivery){
        Integer poidsD = delivery.getPoids();
        User livreur =delivery.getLivreur();

        Integer poidLivreur =livreur.getMaxPoids();

        poidLivreur=poidLivreur+poidsD;
        return poidLivreur;

    }


    public User findNearestDelivery(User client) {
        // selectionner les liste de livreur
        List<User> deliveryUsers = userRepo.findLivreurDisponible();

        // initialiser minDistance et nearestDeliveryUser
        User nearestDeliveryUser = null;
        double minDistance = Double.MAX_VALUE;

        // Calculer la distance entre client and chaque livreur
        for (User deliveryUser : deliveryUsers) {
            if (deliveryUser.getLatitude() != null && deliveryUser.getLongitude() != null) {
                double distance = distance(client.getLatitude(), client.getLongitude(),
                        deliveryUser.getLatitude(), deliveryUser.getLongitude());

                // mise a jour  le livrur le plus proche  and minimum distance
                if (distance < minDistance) {
                    nearestDeliveryUser = deliveryUser;
                    minDistance = distance;
                }
            }
        }
     //   if (conditionpoids(nearestDeliveryUser))
       //     return nearestDeliveryUser;

        return nearestDeliveryUser;
    }

    private static final double EARTH_RADIUS = 6371.01; // kilometers

    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }


    public boolean verifierProximite(double latitudeLivreur, double longitudeLivreur,
                                     double latitudeClient, double longitudeClient) {

        // Calcul de la distance entre le livreur et le client en utilisant la formule Haversine
        double earthRadius = 6371000; // rayon de la terre en mètres
        double dLat = Math.toRadians(latitudeClient - latitudeLivreur);
        double dLng = Math.toRadians(longitudeClient - longitudeLivreur);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(latitudeLivreur)) * Math.cos(Math.toRadians(latitudeClient))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        // Vérification de la proximité
        if (distance < 100) {
            return true;
        } else {
            return false;
        }

    }


}






