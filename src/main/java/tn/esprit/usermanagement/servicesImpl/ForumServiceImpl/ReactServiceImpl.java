package tn.esprit.usermanagement.servicesImpl.ForumServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.entities.ForumEntities.Post;
import tn.esprit.usermanagement.entities.PostComment;
import tn.esprit.usermanagement.entities.ForumEntities.React;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.enumerations.ReactType;
import tn.esprit.usermanagement.repositories.PostCommentRepo;
import tn.esprit.usermanagement.repositories.PostRepo;
import tn.esprit.usermanagement.repositories.ReactRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.ForumIservice.ReactIservice;
import tn.esprit.usermanagement.servicesImpl.AuthenticationService;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class ReactServiceImpl implements ReactIservice {
    ReactRepo reactRepository;
    UserRepo userRepo;
    PostRepo postRepo;
    PostCommentRepo postCommentRepo;
    AuthenticationService authenticationService;
    //React For Post

    public React addReactToPost( Integer postId, ReactType reactType) {

User user = authenticationService.currentlyAuthenticatedUser();
Post post = postRepo.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post not found"));
        React oldReact =  reactRepository.findByPostAndUser(post,user);
        if (oldReact != null)
        {
            oldReact.setType(reactType);
            return reactRepository.save(oldReact);}
        else
        {

            React react = new React();
            react.setUser(user);
            react.setPost(post);
            react.setType(reactType);
            return reactRepository.save(react);
        }
    }

    public void deleteReact(Integer reactId) {

        reactRepository.deleteById(reactId);
    }

    public Post getMostReactedPost(ReactType reactType) {
        List<Post> postList = postRepo.findAll();
        if (reactType != null) {
            return postList.stream().max(Comparator.comparingInt(post -> post.getReacts().stream()
                    .filter(react -> react.getType().equals(reactType)).toArray().length)).orElse(null);
        } else {
            return postList.stream().max(Comparator.comparingInt(post -> post.getReacts().toArray().length)).orElse(null);

        }
    }
    public List<React> getAllReactsForPost(Post post) {

        return reactRepository.findByPost(post);
    }
    public List<React> getAllReactionsByPostIdAndOwner(Integer postId) {
        return reactRepository.findByPostIdAndUser(postId, authenticationService.currentlyAuthenticatedUser().getId());
    }
    public User findMostReactiveUsers() {
        User mostReactive= new User();
        Integer max=0;
        for (User user: userRepo.findAll())
        {
            if (reactRepository.countByUser(user)>max)
            {
                max=reactRepository.countByUser(user);
                mostReactive=user;
            }
        }


        return mostReactive;
    }


    public React addReactToComment( Integer commentId, ReactType reactType) {
        User user = authenticationService.currentlyAuthenticatedUser();
        PostComment comment = postCommentRepo.findById(commentId).orElseThrow(() -> new EntityNotFoundException("comment not found"));
        React oldReact =  reactRepository.findByCommentAndUser(comment,user);
        if (oldReact != null)
        {
            oldReact.setType(reactType);
            return reactRepository.save(oldReact);}
        else
        {

            React react = new React();
            react.setUser(user);
            react.setComment(comment);
            react.setType(reactType);
            return reactRepository.save(react);
        }
    }
    public List<React> getAllReactsForComment(PostComment comment) {
        return reactRepository.findByComment(comment);
    }

    public List<React> getAllReactionsByCommentIdAndOwner(Integer commentId, Integer userId) {
        return reactRepository.findAllByCommentIdAndUser(commentId, userId);
    }

}
