package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.Query;
import tn.esprit.usermanagement.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.usermanagement.enumerations.OrdersStatus;

import java.util.List;

public interface OrderRepo extends JpaRepository<Orders,Integer> {
    @Query("select o from Orders o where o.ordersStatus='AVECLIVRAISON'")
    public List<Orders> findByStatus();



}
