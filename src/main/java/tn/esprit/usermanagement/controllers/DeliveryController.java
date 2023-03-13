package tn.esprit.usermanagement.controllers;

import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.Delivery;
import tn.esprit.usermanagement.entities.Orders;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.enumerations.OrdersStatus;
import tn.esprit.usermanagement.enumerations.Role;
import tn.esprit.usermanagement.repositories.DeliveryRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.servicesImpl.AddressService;
import tn.esprit.usermanagement.servicesImpl.DeliveryServiceImpl;
import tn.esprit.usermanagement.servicesImpl.GeolocationService;

import java.net.URI;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/deliveries")
public class DeliveryController {
    private DeliveryServiceImpl deliveryService;
    private UserRepo userRepo;
    AddressService service;

    private DeliveryRepo deliveryRepo;

    @GetMapping(path = "/show")
    public List<Delivery> findAll() {

        return deliveryService.findAll();
    }

    @PostMapping
    public ResponseEntity<Delivery> create(@RequestBody Delivery delivery) {
        Delivery savedDelivery = deliveryService.save(delivery);
        return ResponseEntity.created(URI.create("/deliveries/" + savedDelivery.getId()))
                .body(savedDelivery);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Delivery> findById(@PathVariable("id") Integer id) {
        Optional<Delivery> delivery = deliveryService.findById(id);
        return delivery.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Delivery> update(@PathVariable("id") Integer id, @RequestBody Delivery delivery) {
        if (!id.equals(delivery.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Delivery updatedDelivery = deliveryService.save(delivery);
        return ResponseEntity.ok(updatedDelivery);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Integer id) {
        deliveryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Delivery> rechercherDeliveryList(@RequestParam("dest") String dest) {
        return deliveryService.rechercherDeliveryList(dest);
    }

    //La méthode createDelivery() appelle la méthode attribuerLivraison() du service de livraison pour attribuer la livraison à un utilisateur ayant le rôle "DELIVERY", puis renvoie la livraison créée dans une réponse HTTP 200 OK.
    @PostMapping("/attribuer_livraison")
    public ResponseEntity<Delivery> createDelivery(@RequestBody Delivery delivery) {
        deliveryService.attribuerLivraison(delivery);
        return ResponseEntity.ok(delivery);

    }

    @GetMapping("/return")
    public List<User> returnlivreuranduserfromdel(@RequestParam Integer id) {
        return deliveryService.returnUserLivreurfromdel(id);

    }

    @PostMapping("/driver/{deliveryId}")
    public ResponseEntity<User> assignDriver(@PathVariable("deliveryId") Integer deliveryId) {
        User delivery1 = deliveryRepo.findUserFromOrders(deliveryId);
       User user = deliveryService.findNearestDelivery(delivery1);
        boolean success = deliveryService.assignDriverr(deliveryId);
        if (success) {
            return ResponseEntity.ok(user);

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(user);
        }
    }

    @GetMapping("/{livreurId}/list/{clientId}")
    public List<Delivery> findDeliveriesByLivreurIdAndClientIdAndPoids(@PathVariable Integer livreurId, @PathVariable Integer clientId) {
        return deliveryService.findDeliveriesByLivreurIdAndClientIdAndPoids(livreurId, clientId);


//afficher les livreur disponible
    }

    @GetMapping("/livreurDisponible")
    public List<User> findLivreurDisponible() {
        return deliveryService.getAvailableDeliveryUsers();


    }

    @GetMapping("/listeDesLivraisonParLvreur/{idlivreur}")
    public List<Delivery> chercherlisteDesLivraisonParLvreur(@PathVariable("idlivreur") Integer idlivreur) {
        return deliveryService.findLivreurDeliveriess(idlivreur);
    }


    @PostMapping("/affecterDeliveries")
    public ResponseEntity<String> affecterDeliveries() {
        deliveryService.affecterDeliveries();
        return ResponseEntity.ok("Les livraisons ont été affectées avec succès.");
    }


    @PostMapping("/confirme/{iddelivery}")
    public Delivery confirmerlivraison(@PathVariable("iddelivery") Integer iddelivery) {
        return deliveryService.confirmDeliveryReceived(iddelivery);

    }


    @GetMapping("/{clientId}/nearestDelivery")
    public ResponseEntity<User> getNearestDelivery(@PathVariable Integer clientId) {
        // Find the client user by id
        Optional<User> clientOptional = userRepo.findById(clientId);
        if (!clientOptional.isPresent() || clientOptional.get().getRole() != Role.USER) {
            return ResponseEntity.notFound().build();
        }
        User client = clientOptional.get();

        // Find the nearest delivery user
        User nearestDelivery = deliveryService.findNearestDelivery(client);
        if (nearestDelivery == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(nearestDelivery);
    }

    @PostMapping("/adresse/{iduser}")
    public String trouveradresse(@PathVariable("iduser") Integer iduser) {
        return service.retrouveradresseUSER(iduser);

    }

    @GetMapping("/gg/{lat}/{lon}")
    public String gdddddg(@PathVariable("lat") Double lat, @PathVariable("lon") Double lon ) {
        return service.getAddress(lat,lon);
    }


}