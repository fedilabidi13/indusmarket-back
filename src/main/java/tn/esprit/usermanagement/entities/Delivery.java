package tn.esprit.usermanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Integer ClientDd;



    @OneToMany(cascade = CascadeType.ALL, mappedBy="deliveryS")
    private List<Orders> Orders;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id")
    private User livreur;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy="deliveryId")
    private List<Evaluation> evaluations;

}
