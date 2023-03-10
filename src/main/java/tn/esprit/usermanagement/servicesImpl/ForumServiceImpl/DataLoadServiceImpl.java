package tn.esprit.usermanagement.servicesImpl.ForumServiceImpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.entities.CategoryAdve;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.entities.UserDataLoad;
import tn.esprit.usermanagement.repositories.CategoryAdverRepo;
import tn.esprit.usermanagement.repositories.UserDataLoadRepo;
import tn.esprit.usermanagement.repositories.UserRepo;

import java.util.List;
@Service
@AllArgsConstructor
public class DataLoadServiceImpl {
    UserDataLoadRepo userDataLoadRepo;
    UserRepo userRepo;
    CategoryAdverRepo categoryAdverRepo;
    // detection des champ por ajouter dataUseradv
    public Boolean existDataForUser(String ch,Integer IdUser) {
        Boolean x = false;
        for (UserDataLoad userDataLoad : userDataLoadRepo.findAll()) {
            if (userDataLoad.getCategorieData().equals(ch) && userDataLoad.getUser().getId() == IdUser) {
                x = true;
            }
        } return x;
    }
    public UserDataLoad getData(String ch, Integer IdUser) {
        UserDataLoad x = null;
        for (UserDataLoad userDataLoad : userDataLoadRepo.findAll()) {
            if (userDataLoad.getCategorieData().equals(ch) && userDataLoad.getUser().getId() == IdUser) {
                x = userDataLoad;
            }
        } return x;
    }
    public void DetctaDataLoad (String ch , Integer idUser) {

        List<UserDataLoad> ul = userDataLoadRepo.findAll();
        User u = userRepo.findById(idUser).orElse(null);
        for (CategoryAdve string : categoryAdverRepo.findAll()) {
            if (ch.contains(string.getNameCategory())) {
                if (existDataForUser(string.getNameCategory(),idUser) == true) {
                    UserDataLoad l = getData(string.getNameCategory(),idUser);
                    l.setNbrsRequet(l.getNbrsRequet()+1);
                    userDataLoadRepo.save(l);
                }
                else {
                    UserDataLoad l1 = new UserDataLoad();
                    l1.setCategorieData(string.getNameCategory());
                    l1.setUser(u);
                    l1.setNbrsRequet(1);
                    userDataLoadRepo.save(l1);

                }
            }
        }
    }


}
