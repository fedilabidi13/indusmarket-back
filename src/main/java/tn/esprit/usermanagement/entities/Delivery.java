package tn.esprit.usermanagement.entities;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.usermanagement.enumerations.DeliveryStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Delivery implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idDelivery")
    private Integer id;
    private String code;
    @Temporal(TemporalType.DATE)
    private Date deliveryDate;
    @Temporal(TemporalType.DATE)
    private Date deliveredDate;
    @Column(name = "poids")
    private int poids;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    @Column(nullable = false)
    private String ville;
    @Column(nullable = false)
    private String destinataire;


    @OneToMany(cascade = CascadeType.ALL, mappedBy="deliveryS")
    private List<Orders> Orders;

    @ManyToOne
    @JoinColumn(name = "id")
    private User livreur;


    @OneToMany(cascade = CascadeType.ALL, mappedBy="commande")
    private List<Evaluation> evaluations;
}
