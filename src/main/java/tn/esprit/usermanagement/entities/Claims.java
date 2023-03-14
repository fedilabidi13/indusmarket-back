package tn.esprit.usermanagement.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.usermanagement.entities.ForumEntities.Post;
import tn.esprit.usermanagement.enumerations.StatusClaims;
import tn.esprit.usermanagement.enumerations.TypeClaim;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table( name = "Claims")
public class Claims implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idClaims")
    private Integer idClaims;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private TypeClaim typeClaim;
    @Enumerated(EnumType.STRING)
    private StatusClaims statusClaims;
    private LocalDateTime CreatedAt;
    private LocalDateTime   ConsultAt;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @JsonIgnore
    @ManyToOne
    private Orders order;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Media> medias;
    @JsonIgnore
    @ManyToOne
    private Post post;
    @OneToMany(mappedBy = "claims", cascade = CascadeType.ALL)
    private List<ClaimProductRef> claimProductRefs;
}
