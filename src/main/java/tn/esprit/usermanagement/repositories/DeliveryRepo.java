package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.Delivery;
import tn.esprit.usermanagement.entities.Evaluation;
import tn.esprit.usermanagement.entities.Orders;
import tn.esprit.usermanagement.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepo extends JpaRepository<Delivery,Integer> {
    @Query("select d from Delivery d where  d.destinataire like :dest ")
    public List<Delivery> findByDestinataire(@Param("dest") String dest);
 //   @Query("select d from Delivery d where d.id like :id")
    //public List<Delivery> findByIdDelivery(Integer id);

    public Optional<Delivery> findById(Integer id);

    Optional<Evaluation> findById(Delivery id);

    @Query("select d from Delivery d where d.id = :idDelivery")
    public Delivery findDeliveryBySonId(Integer idDelivery);



    @Query("SELECT u FROM User u INNER JOIN Delivery d where d.id = :idDelivery AND d.livreur.id = u.id ")
    public User ReturnLivreurFromDeliveryId(@Param("idDelivery") Integer idDelivery);


    @Query("select o.user from Orders o  where o.deliveryS.id=:idDelivery")
    public User findUserFromOrders(Integer idDelivery);


    @Query("select d.ClientDd from Delivery d where d.id=:iddelivery")
    public Integer findClientDDfromDlivery(Integer iddelivery);


    @Query("SELECT u FROM Orders o INNER Join User u where o.deliveryS.id = : idDelivery and o.user.id = u.id ")
    public User ReturnClientFromCommande(@Param("idDelivery") Integer idDelivery);

 //   @Query("SELECT d FROM Delivery d  WHERE d.livreur.id = :livreurId AND d.Orders. = :clientId AND d.poids = :poids")
    //List<Delivery> findDeliveriesByLivreurIdAndClientIdAndPoids(Integer livreurId, Integer clientId, Float poids);

    public List<Delivery> findByLivreur(User livreur);

    //afficher la liste des livraison en attente
    @Query("select d from Delivery d where d.status='PENDING'")
    public List<Delivery> findDeliveryEnAttente();

    //afficher la liste des nouvelle livraison
    @Query("select d from Delivery d where d.status='NEW'")
    public List<Delivery> findDeliveryNew();


    //afficher la liste des nouvelle livraison
    @Query("select d from Delivery d where d.status='DELIVERED'")
    public List<Delivery> findDeliveryDelivered();


    // afficher la liste des livraison pour chaque livreur
    @Query("select d from User u JOIN u.deliveries d where u.id =?1 ")
    public List<Delivery> findLivreurDeliveries(  Integer idlivreur);

@Query("select d from  Delivery d where d.status='New'")
public List<Delivery> findByStatus(String DeliveryStatus);

@Query("select d from Delivery d where d.id=:iduser")
    public Delivery findDeliveryByUSER(Integer  iduser);}



