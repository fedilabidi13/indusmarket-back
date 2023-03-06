package tn.esprit.usermanagement.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.Rating;

import java.util.List;

@Repository
public interface RateRepository extends JpaRepository<Rating, Integer> {


    @Query("SELECT AVG(r.value) FROM Rating r WHERE r.shop.idShop = ?1")
    Double getAverageRatingForShop( Integer shopId);
    @Query("select r from Rating r where r.shop.idShop=?1 ")
    List<Rating> findByShopId(Integer shopId);
}
