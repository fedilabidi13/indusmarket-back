package tn.esprit.usermanagement.servicesImpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.entities.Trajet;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.services.TrajetService;

import java.util.List;

@Service
@AllArgsConstructor
public class TrajetServiceImpl implements TrajetService {




/*
    @Override
    public Trajet trouverTrajetMin(User clientAdresse, List<User> livreurs) {
        User livreurPlusProche = null;
        double distanceMin = Double.MAX_VALUE;
        for (User livreur : livreurs) {
            User livreurAdresse = livreur.getAdresse();
            double distance = geocode.calculerDistance(clientAdresse, livreurAdresse);
            if (distance < distanceMin) {
                distanceMin = distance;
                livreurPlusProche = livreur;
            }
        }
        if (livreurPlusProche == null) {
            throw new ResourceNotFoundException("Livreur non trouvé");
        }
        Adresse livreurAdresse = livreurPlusProche.getAdresse();
        List<Adresse> adresses = new ArrayList<>();
        adresses.add(clientAdresse);
        adresses.add(livreurAdresse);
        for (Commande commande : livreurPlusProche.getCommandes()) {
            adresses.add(commande.getAdresseLivraison());
        }
        Trajet trajet = aStar.calculerTrajet(adresses);
        return trajet;
    }*/
}
