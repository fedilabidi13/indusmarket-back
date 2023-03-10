package tn.esprit.usermanagement.entities;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
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
public class PostComment implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    String commentBody;

    LocalDateTime commentedAt;


    @ManyToOne
            @JsonIgnore
    User user; // The user who wants to comment

    @JsonIgnore
    @ManyToOne
    Post post; // The post to comment


    @ManyToOne
    @JsonIgnore
    PostComment postComment;



    @OneToMany(cascade = CascadeType.ALL)
    private List<Pictures> pictures;


}

