package tn.esprit.usermanagement.entities.ForumEntities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table( name = "Pictures")
public class Pictures implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "LONGBLOB")
    @Lob
    private byte[] data;
    private String contentType;

    public Pictures(Integer id, String name, String imagenUrl, String codeImage) {
    }
}
