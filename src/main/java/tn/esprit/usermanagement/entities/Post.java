package tn.esprit.usermanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
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

    Date createdAt;

    String body;




    @ManyToOne
    User user;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    List<React> postReacts;




    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    List<PostComment> postComments;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    List<User> reportedby;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    List<Media> medias;
}
