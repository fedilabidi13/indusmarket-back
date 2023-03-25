package tn.esprit.usermanagement.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "user_search_history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ElementCollection
    private List<String> search;
    @ManyToOne
    private User user;


}






