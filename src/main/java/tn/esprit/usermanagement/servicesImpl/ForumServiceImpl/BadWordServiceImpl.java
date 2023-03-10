package tn.esprit.usermanagement.servicesImpl.ForumServiceImpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.entities.ForumEntities.BadWord;
import tn.esprit.usermanagement.repositories.BadWordRepo;
import tn.esprit.usermanagement.services.ForumIservice.BadWordIservice;

import java.util.List;

@Service
@AllArgsConstructor
public class BadWordServiceImpl implements BadWordIservice {
    BadWordRepo badWordRepo;
    //BadWord
    public int Filtrage_bad_word(String ch) {
        int x = 0;
        List<BadWord> l1 = (List<BadWord>) badWordRepo.findAll();
        for (BadWord badWord : l1) {
            // if (badWord.getWord().contains(ch))
            if (ch.contains(badWord.getWord()) == true)
                x = 1;
        }
        return x;

    }
    public BadWord addBadWord(BadWord b ) {

        return badWordRepo.save(b);
    }
    public  BadWord  updateBadWord(  BadWord b   ) {
        return   badWordRepo.save(b);
    }
    public void removeBadWord(Integer idBadWord){
        badWordRepo.deleteById(idBadWord);
    }
    public List<BadWord>  GetAllBadWords()  {
        return  badWordRepo.findAll();
    }
    public   BadWord   GetBadWord (Integer  idBadWord)  {
        return badWordRepo.findById(idBadWord).orElse(null);
    }


}
