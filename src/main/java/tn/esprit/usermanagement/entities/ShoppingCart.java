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
public class ShoppingCart implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shoppingCart")
    @JsonIgnore
    private List<CartItem> cartItemList;


    public void removeCartItem(CartItem item) {
        this.cartItemList.remove(item);
        item.setShoppingCart(null);
    }

}