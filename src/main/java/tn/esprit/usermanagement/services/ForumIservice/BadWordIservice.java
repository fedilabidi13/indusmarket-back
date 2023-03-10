package tn.esprit.usermanagement.services.ForumIservice;

import tn.esprit.usermanagement.entities.ForumEntities.BadWord;

import java.util.List;

public interface BadWordIservice {
    //BadWord
    int Filtrage_bad_word(String ch);
    BadWord addBadWord(BadWord b ) ;
    BadWord  updateBadWord(  BadWord b   );
    void removeBadWord(Integer idBadWord);
    List<BadWord> GetAllBadWords()  ;
    BadWord   GetBadWord (Integer  idBadWord)  ;
}
