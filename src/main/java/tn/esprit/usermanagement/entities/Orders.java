package tn.esprit.usermanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private  float totalAmount;
    private boolean paid;

 @OneToMany
 @JsonIgnore
 private List<CartItem> secondCartItemList;


    @JsonIgnore
    @ManyToOne
    private User user;
    private  String dilevryAdresse;
    @JsonIgnore
    @OneToOne(mappedBy = "ordre")
    private Invoice invoice;
    @JsonIgnore
    @ManyToOne
    private Delivery deliveryS;
}