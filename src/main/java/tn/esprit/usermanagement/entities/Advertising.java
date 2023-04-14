package tn.esprit.usermanagement.entities;



import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import tn.esprit.usermanagement.entities.ForumEntities.Media;
import tn.esprit.usermanagement.entities.ForumEntities.Pictures;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@ToString
public class Advertising implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    String Name;

    @JsonIgnore

    int minage;
    int maxage;

    String text;

    @ManyToOne
    CategoryAdve categoryadv;

    LocalDateTime StartDate;

    @Temporal(TemporalType.DATE)
    Date EndDate;

    int nbrIntialViews;

    int nbrFinalViews;

    float price;


    @JsonIgnore
    @ManyToOne
    User user;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Pictures> pictures;
}