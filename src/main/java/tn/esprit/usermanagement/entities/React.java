package tn.esprit.usermanagement.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.attoparser.dom.Comment;
import tn.esprit.usermanagement.enumerations.ReactType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@ToString
public class React implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private ReactType type;

    @ManyToOne
            @JsonBackReference
    User user; // The user who clicked Like

@JsonBackReference
    @ManyToOne
    Post post; // The post to react

    @JsonBackReference
    @ManyToOne
    PostComment comment; // The post to react






}
