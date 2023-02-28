package tn.esprit.usermanagement.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.entities.Shop;

import java.util.List;

@Repository

public interface ShopRepo extends JpaRepository<Shop,Integer> {
    @Query("select s from Shop s join s.user u where u.id =?1")
    public List<Shop> ShowAllShops(int idUser);
    @Query("select p from Product p join Shop s  where s.idShop=?1 order by p.category")
    public List<Product> GenerateCatalog(int idShop);
    @Query("select s from Shop s ")
    public List<Shop> ShowAllShops();
}
