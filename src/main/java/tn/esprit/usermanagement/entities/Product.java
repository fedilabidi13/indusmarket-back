package tn.esprit.usermanagement.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.usermanagement.enumerations.Category;

import java.io.Serializable;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table( name = "Product")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProduct")
    private Integer idProduct;
    private String reference;
    private String name;
    private Integer quantity;
    private float price;
    private String description;
    private Integer discount;
    private float PriceAfterDiscount;
    private String brand;
    private String status;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Lob
    private byte[] BarcodeImage;


    @OneToMany(cascade = CascadeType.ALL)
    private List<Pictures> pictures;

    @ManyToOne
    private Shop shop;


}