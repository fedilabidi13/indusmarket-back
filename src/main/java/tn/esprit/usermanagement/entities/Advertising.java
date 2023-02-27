package tn.esprit.usermanagement.entities;



import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

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

    @Temporal(TemporalType.DATE)
    Date StartDate;

    @Temporal(TemporalType.DATE)
    Date EndDate;

    int nbrIntialViews;

    int nbrFinalViews;

    float price;


    @JsonIgnore
    @ManyToOne
    User user;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "advertising")
    List<Media> medias;
}