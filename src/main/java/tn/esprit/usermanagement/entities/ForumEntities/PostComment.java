package tn.esprit.usermanagement.entities.ForumEntities;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.usermanagement.entities.User;

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
public class PostComment implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    String commentBody;

    LocalDateTime commentedAt;

    @ManyToOne
    User user; // The user who wants to comment

    @ManyToOne
    Post post; // The post to comment


    @ManyToOne
    @JsonIgnore
    PostComment postComment;


    @OneToMany
    List<Media> medias;
    @JsonIgnore
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comment")
    private List<React> reacts;

}

