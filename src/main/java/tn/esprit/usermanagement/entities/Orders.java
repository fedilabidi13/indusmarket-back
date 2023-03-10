package tn.esprit.usermanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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
    private LocalDateTime creationDate;

    @JsonIgnore
    @ManyToOne
    private User user;
    private float amount=0;

    private boolean Payed=false;
    @JsonIgnore
    @OneToOne(mappedBy = "ordre")
    private Invoice invoice;
    @JsonIgnore
    @ManyToOne
    private Delivery deliveryS;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy="order")
    private List<Claims> claims;
}