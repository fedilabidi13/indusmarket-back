package tn.esprit.usermanagement.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import tn.esprit.usermanagement.entities.Delivery;
import tn.esprit.usermanagement.entities.Orders;
import tn.esprit.usermanagement.entities.User;

@Data
@Getter
@Setter
public class DeliveryDTO {
    private Delivery delivery;
    private User client;
    private Orders orders;

}
