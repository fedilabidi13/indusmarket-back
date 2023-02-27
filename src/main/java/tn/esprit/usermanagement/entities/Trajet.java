package tn.esprit.usermanagement.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Trajet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idTrajet")
    private Integer id;
    private LocalDateTime heureDepart;
    private LocalDateTime heureArriveePrev;
    private String adresseDepart;
    private String adresseArrivee;
    private Double distance;
    private Integer dureeEstimee;
    private String statut;

    @ManyToOne
    private User livreur;

    @ManyToOne
    private Delivery deliveryss;
}
