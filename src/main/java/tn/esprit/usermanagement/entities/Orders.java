package tn.esprit.usermanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
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
    @JsonIgnore
    @ManyToOne
    private User user;
    private float amount=0;
    @JsonIgnore
    @OneToOne(mappedBy = "ordre")
    private Invoice invoice;
    @JsonIgnore
    @ManyToOne
    private Delivery deliveryS;
    @JsonIgnore
    @ManyToMany
    private List<Shop> shops;
}