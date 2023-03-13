package tn.esprit.usermanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Evaluation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEvaluation")
    private Integer id;
    private int note;

    private String commentaire;

    @Column(nullable = false)
    private LocalDateTime dateHeureEvaluation;
    @JsonIgnore

    @ManyToOne
    @JoinColumn(name = "Dilevery_id", nullable = false)
    private Delivery deliveryId;
}
