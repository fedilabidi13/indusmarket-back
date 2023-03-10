package tn.esprit.usermanagement.entities.ChatEntities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.usermanagement.entities.ChatEntities.ChatMessage;
import tn.esprit.usermanagement.entities.User;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@ToString
public class Chatroom implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne
    User sender;
    @JsonIgnore
    @ManyToOne
    User reciver;


    String color = "#EC407A";


    @OneToMany(cascade = CascadeType.ALL , mappedBy = "chat")
    List<ChatMessage> messages;
}
