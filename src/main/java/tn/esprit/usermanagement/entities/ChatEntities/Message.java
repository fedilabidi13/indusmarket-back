package tn.esprit.usermanagement.entities.ChatEntities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.usermanagement.entities.Pictures;
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
public class Message implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer  messageId;

    String body;
    LocalDateTime date;

    @JsonIgnore
    @ManyToOne
    User sender;
    @JsonIgnore
    @ManyToOne
    User reciver;
    @JsonIgnore
    @ManyToOne
    Chatroom chatroom;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Pictures> pictures;



}
