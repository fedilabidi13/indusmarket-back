package tn.esprit.usermanagement.entities;

import jakarta.persistence.*;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.usermanagement.entities.ForumEntities.Media;
import tn.esprit.usermanagement.entities.ForumEntities.Pictures;
import tn.esprit.usermanagement.enumerations.Category;

import java.time.LocalDateTime;
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
    @OneToMany( fetch = FetchType.EAGER)
    private List<Media> medias;

    @ManyToOne
    private Shop shop;
    @ManyToOne
    private Stock stock;
    private Boolean validated;
    private LocalDateTime soldAt;
    ////Houssem Association
    @JsonIgnore
    @ManyToMany
    private List<Orders> orders;
    private Boolean oneTimeEmail;
}