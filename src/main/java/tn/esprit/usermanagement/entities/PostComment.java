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
public class PostComment implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    String commentBody;

    Date commentedAt;


    @ManyToOne
    User user; // The user who wants to comment

    @JsonIgnore
    @ManyToOne
    Post post; // The post to comment


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postCo")
    List<PostComment> postComments; //Reflexive association : A comment can have multiple replies
    @JsonIgnore
    @ManyToOne
    PostComment postCo;


    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "postComment")
    List<CommentLike> commentLikes;

}

