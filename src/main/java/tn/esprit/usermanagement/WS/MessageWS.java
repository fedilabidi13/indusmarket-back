package tn.esprit.usermanagement.WS;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@ToString
public class MessageWS implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long messageId;
	String body;
	@JsonIgnore
	@ManyToOne
	ChatroomWS chatWS;
}
