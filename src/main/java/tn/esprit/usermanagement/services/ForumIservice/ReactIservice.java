package tn.esprit.usermanagement.services.ForumIservice;

import tn.esprit.usermanagement.entities.ForumEntities.Post;
import tn.esprit.usermanagement.entities.ForumEntities.React;
import tn.esprit.usermanagement.entities.ForumEntities.PostComment;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.enumerations.ReactType;

import java.util.List;

public interface ReactIservice {
    //React For Post
    React addReactToPost( Integer postId, ReactType reactType) ;

    void deleteReact(Integer reactId) ;
    Post getMostReactedPost(ReactType reactType) ;

    List<React> getAllReactsForPost(Post post) ;
    public List<React> getAllReactionsByPostIdAndOwner(Integer postId) ;


        User findMostReactiveUsers();

    //React For Comment

    React addReactToComment(Integer commentId, ReactType reactType) ;
    public List<React> getAllReactsForComment(PostComment comment) ;


    }
