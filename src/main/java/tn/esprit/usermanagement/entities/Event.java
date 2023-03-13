package tn.esprit.usermanagement.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table( name = "Event")
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;
    private String currency;
    private String method;
    private String intent;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endDate;
    private String adresse;
    public boolean accepted=false;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private List<Pictures> pictures;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy="event")
    private List<Ticket> tickets;
    @JsonIgnore
    @ManyToOne
    User user;
    private double price;
    @JsonIgnore
    @OneToOne
    private Address address;
}
