package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.enumerations.Role;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepo extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    User findByEmail2(String email);
    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableUser(String email);
    List<User> findByRole(Role role);
    @Query("SELECT u FROM User u WHERE u.id = ?1")
    User findById2(int userId);

    User findByPhoneNumber(Integer phoneNumber);


    //ABDELMALEK BACCAR

    // selectioner les utilisateur ayant le role DELIVERY
    @Query("SELECT u.id FROM User u WHERE u.role = 'DELIVERY' ")
    List<User> findDeliveryUsers();
//une méthode de recherche qui prend en paramètre le numero de telephone et renvoie l'adresse de l'utilisateur qui a un rôle "USER" :
    @Query("SELECT u.Adresse FROM User u WHERE u.phoneNumber = :phoneNumber AND u.role = 'USER'")
    public String findUserAddressByphoneNumber(@Param("phoneNumber") String username);

    @Query("SELECT u FROM User u WHERE u.role = 'DELIVERY' AND u.etatLivreur = 'Disponible' AND u.id NOT IN (SELECT d.livreur.id FROM Delivery d WHERE d.status = 'PENDING')")
    List<User> findAvailableDelivery();
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.role = :role")
    User findByIdAndRole(@Param("id") Integer id, @Param("role") Role role);



    //afficher la liste des livreur disponible
    @Query(" select u from User u where u.role ='DELIVERY' and u.etatLivreur = 'DISPONIBLE' ")
    public List<User> findLivreurDisponible();

    //afficher la liste des livreur disponible
    @Query(" select u from User u where u.role ='DELIVERY' and u.etatLivreur = 'OCUPPE' ")
    public List<User> findLivreurOcuppe();

}
