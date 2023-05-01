package tn.esprit.usermanagement.WS;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.usermanagement.entities.ChatEntities.Chatroom;


/**
 * POJO representing the chat message
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ChatMessageWS {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long messageId;
    private String text;
    private String username;
    private String avatar;
	Long idchat;
	Long senderWS;
	@JsonIgnore
	@ManyToOne
    ChatroomWS chatWS;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

	
}
