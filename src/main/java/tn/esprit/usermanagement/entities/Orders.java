package tn.esprit.usermanagement.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Orders implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private User user;
    private float amount=0;
    @OneToOne(mappedBy = "ordre")
    private Invoice invoice;
    @ManyToOne
    private Delivery deliveryS;
}