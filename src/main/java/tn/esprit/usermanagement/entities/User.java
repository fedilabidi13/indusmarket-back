package tn.esprit.usermanagement.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tn.esprit.usermanagement.enumerations.BanType;
import tn.esprit.usermanagement.enumerations.Role;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="_user")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Boolean enabled= false;
    private Boolean firtAttempt = true;
    private Boolean emailVerif;
    private Boolean phoneNumberVerif;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private BanType banType;
    private LocalDateTime bannedAt;
    @OneToMany(mappedBy = "user")
    private List<JwtToken> jwtTokens;

    // Delivery attributes
    private Integer phoneNumber;
    private String secteur;
    @Column(name = "max_poids")
    private int maxPoids;
    @Column(name = "nombres_des_commandes ")
    private int nbrCommande;
    private String listeColis;
    private String listeClient ;
    @OneToMany(cascade = CascadeType.ALL, mappedBy="livreur")
    private List<Delivery> deliveries;

    // Shopping cart and order
    @OneToOne
    private ShoppingCart shoppingCart;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Orders> ordersList;

    // Shop
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    private List<Shop> shops;
    @OneToMany(mappedBy = "user1")
    private List<Rating> rates;
    //todo Picture
    //todo more attributes (shops products address phone number )
    // Specefic Forum Attributes
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    List<Post> posts;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    List<React> postReacts;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    List<PostComment> postComments;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    List<CommentLike> commentLikes;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    List<Advertising> advertising;

    ////Association Oussama
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
    private List<Claims> claims;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
    private List<Ticket> tickets;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
    private List<Event> events;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
