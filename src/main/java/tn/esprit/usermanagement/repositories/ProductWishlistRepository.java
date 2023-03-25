package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.entities.ProductWishlist;
import tn.esprit.usermanagement.entities.User;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProductWishlistRepository extends JpaRepository<ProductWishlist,Integer> {
    List<ProductWishlist> findByUser(User user);
    @Query("SELECT pw FROM ProductWishlist pw WHERE pw.user = :user AND pw.product = :product")
    Optional<ProductWishlist> findByUserAndProduct(@Param("user") User user, @Param("product") Product product);

}
