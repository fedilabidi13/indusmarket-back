package tn.esprit.usermanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    private String InvoiceRef;


    @JsonIgnore
    @ManyToOne
    private User user;
    private float amount;


    @JsonIgnore
    @OneToOne(mappedBy = "ordre")
    private Invoice invoice;
    @JsonIgnore
    @ManyToOne
    private Delivery deliveryS;
}