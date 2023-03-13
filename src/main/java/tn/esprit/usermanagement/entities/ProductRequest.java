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

public class ProductRequest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer quantityNeeded;
    @ManyToOne
    private User user;
    @ManyToOne
    private Product product;

}