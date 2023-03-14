package tn.esprit.usermanagement.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.usermanagement.entities.ForumEntities.Pictures;

import java.io.Serializable;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table( name = "Ticket")
public class Ticket implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String reference;
    private String descreption;
    @JsonIgnore
    @ManyToOne
    private User user;
    @JsonIgnore
    @ManyToOne
    private Event event;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Media> medias;
}
