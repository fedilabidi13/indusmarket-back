package tn.esprit.usermanagement.servicesImpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.repositories.UserRepo;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepo userRepo;
    public int enableAppUser(String email) {
        return userRepo.enableUser(email);
    }
/*
    public Coordonnees getCoordonneesLivreur(Long id) {
        // code pour récupérer les coordonnées géographiques du livreur avec OpenStreetMap
        Coordonnees coordonnees = new Coordonnees(lat, lon);}*/
}
