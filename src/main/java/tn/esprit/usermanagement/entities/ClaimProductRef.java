package tn.esprit.usermanagement.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table( name = "ClaimProductRef")
public class ClaimProductRef implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String productRef;
    private Integer quantity;
    @JsonIgnore
    @ManyToOne
    private Claims claims;
}
