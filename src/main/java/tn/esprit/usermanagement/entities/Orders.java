package tn.esprit.usermanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.usermanagement.enumerations.OrdersStatus;

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
    @JsonIgnore
    @ManyToOne
    private User user;
    private float totalAmount=0;
    private Boolean paid;
    @Enumerated(EnumType.STRING)
    private OrdersStatus ordersStatus;
    @JsonIgnore
    @OneToOne(mappedBy = "ordre")
    private Invoice invoice;
    private LocalDateTime creationDate;
    @OneToMany
    @JsonIgnore
    private List<CartItem> secondCartItemList;
    @JsonIgnore
    @ManyToOne
    private Delivery deliveryS;
    private String dilevryAdresse;

@JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products = new ArrayList<>(); // initialize the list to an empty ArrayList


}