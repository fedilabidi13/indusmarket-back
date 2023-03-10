package tn.esprit.usermanagement.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@ToString
public class Post implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    String postTitle;

    LocalDateTime createdAt;

    String body;




    @JsonIgnore
    @JsonBackReference
    @ManyToOne
    User user;







    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    List<Media> medias;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Pictures> pictures;
    @JsonIgnore
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<React> reacts;


    //Association Oussama
    @JsonIgnore
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy="post")
    private List<Claims> claims;
}
