package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.usermanagement.entities.ProductWishlist;
import tn.esprit.usermanagement.entities.User;

import java.util.List;

public interface ProductWishlistRepository extends JpaRepository<ProductWishlist,Integer> {
    List<ProductWishlist> findByUser(User user);
}
