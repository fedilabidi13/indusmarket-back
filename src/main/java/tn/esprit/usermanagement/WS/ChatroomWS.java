package tn.esprit.usermanagement.WS;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
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
public class ChatroomWS implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long chatroomId;
	@JsonIgnore
	@ManyToOne
	User senderWS;
	@JsonIgnore
	@ManyToOne 
	User reciverWS;
	String color = "#EC407A";
	@OneToMany(cascade = CascadeType.ALL , mappedBy = "chatWS")
	List<ChatMessageWS> messages;
}
