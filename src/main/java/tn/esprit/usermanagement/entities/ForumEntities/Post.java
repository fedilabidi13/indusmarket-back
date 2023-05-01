package tn.esprit.usermanagement.entities.ForumEntities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.usermanagement.entities.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


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

@OneToMany
List<PostComment> postComment;


    @ManyToOne
    User user;







    @OneToMany
    List<Media> medias;
    @JsonIgnore
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<React> reacts;


    //Association Oussama
    @JsonIgnore
    @OneToMany(cascade = CascadeType.PERSIST, mappedBy="post")
    private List<Claims> claims;
}
