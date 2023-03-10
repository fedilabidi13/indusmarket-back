package tn.esprit.usermanagement.servicesImpl;

import com.cloudinary.http44.api.Response;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.ResourceNotFoundException;
import org.springframework.social.facebook.api.Comment;
import org.springframework.stereotype.Service;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.*;
import tn.esprit.usermanagement.enumerations.ReactType;
import tn.esprit.usermanagement.repositories.*;
import tn.esprit.usermanagement.services.ForumIservice;
import tn.esprit.usermanagement.services.PicturesService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ForumService implements ForumIservice {
    MediaService mediaService;
    CategoryAdverRepo categoryAdvrepo;

    CloudinaryService cloudImage;
    CategoryAdverRepo categoryAdverRepo;
    AuthenticationService authenticationService;
    UserDataLoadRepo userDataLoadRepo;
    AdvertisingRepo advertisingRepo;
    PostRepo postRepo;
    UserRepo userRepo;
    PostCommentRepo postCommentRepo;
    BadWordRepo badWordRepo;
    PicturesRepo picturesRepo;
    ReactRepo reactRepository;

    //Post
    public ResponseEntity<?> addPost(List<MultipartFile> files,  Post post, Integer IdUser) throws IOException {

        User u = userRepo.findById(IdUser).get();
        DetctaDataLoad(post.getBody(),IdUser);
        DetctaDataLoad(post.getPostTitle(),IdUser);
        if (Filtrage_bad_word(post.getBody()) == 0 && Filtrage_bad_word(post.getPostTitle()) == 0) {
            post.setUser(u);

            post.setPictures(addimages(files));
           // post.setReacts(new ArrayList<>());
            postRepo.save(post);
            return ResponseEntity.ok().body(post);
        } else
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Bads Word Detected");
    }
    public ResponseEntity<?> Update_post(Post post,List<MultipartFile> files, Integer idPost, Integer idUser) throws IOException {
        Post oldPost = postRepo.getReferenceById(idPost);

        if (Filtrage_bad_word(post.getBody()) == 0 && Filtrage_bad_word(post.getPostTitle()) == 0) {

            if (post.getBody()!=null)
            {
                oldPost.setBody(post.getBody());
            }
            if (post.getPostTitle()!=null)
            {
                oldPost.setPostTitle(post.getPostTitle());
            }
            if (files != null)
            {
                List<Pictures> picturesList = addimages(files);

                oldPost.setPictures(picturesList);
            }
            postRepo.save(oldPost);
            return ResponseEntity.ok().body(oldPost);
        } else
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Bads Word Detected");

    }
    public void deletePost(Integer postId) {

        postRepo.deleteById(postId);
    }

    public List<Post> Get_all_post() {
        List<Post> findAll = postRepo.findAll();
        return findAll;

    }

    public List<Post> Get_post_by_User(Integer idUser) {

        List<Post> postList = new ArrayList<>();
        for(Post post: postRepo.findAll())
        {
            if (post.getUser().getId()==idUser)
            {
                postList.add(post);
            }
        }
        return postList;
    }

    public Post getPostById (Integer id) {
        Post p = postRepo.findById(id).orElse(null);
        return p;
    }
    public List<Post> Searchpost(String ch,Integer id){
        List<Post> ll = new ArrayList<>();
        for (Post post : postRepo.findAll()) {
            if (post.getBody().contains(ch) || post.getPostTitle().contains(ch))
                ll.add(post);
        }
        DetctaDataLoad(ch,id);
        return ll;
    }
    //React For Post

    public React addReactToPost(Integer userId, Integer postId, ReactType reactType) {
        User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
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
    //Comment
    public ResponseEntity<?> addComment_to_Post(PostComment postComment,List<MultipartFile> files, Integer idPost, Integer idUser) throws IOException{
        Post p = postRepo.getReferenceById(idPost);
        User u = userRepo.getReferenceById(idUser);
        DetctaDataLoad(postComment.getCommentBody(),idUser);
        PostComment comment = new PostComment();
        if (Filtrage_bad_word(postComment.getCommentBody()) == 0) {

            comment.setCommentBody(postComment.getCommentBody());
            comment.setCommentedAt(postComment.getCommentedAt());
            comment.setPictures(addimages(files));
            comment.setPost(p);
            comment.setUser(u);
            postCommentRepo.save(comment);
            return ResponseEntity.ok().body(comment.getCommentBody());
        }else

            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Bads Word Detected");
    }
    public ResponseEntity<?> addResponseToComment(Integer commentId,List<MultipartFile> files, PostComment postComment)  throws IOException{
        PostComment comment = new PostComment();
        PostComment originalComment = postCommentRepo.getReferenceById(commentId);
        if (Filtrage_bad_word(postComment.getCommentBody()) == 0) {
            comment.setPostComment(originalComment);
            comment.setUser(authenticationService.currentlyAuthenticatedUser());
            comment.setCommentBody(postComment.getCommentBody());
            comment.setCommentedAt(postComment.getCommentedAt());
            comment.setPost(postComment.getPost());
            comment.setPictures(addimages(files));
            postCommentRepo.save(comment);
            return ResponseEntity.ok().body(comment.getCommentBody());
        } else {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Bads Word Detected");
        }


    }
    public React addReactToComment(Integer userId, Integer commentId, ReactType reactType) {
        User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
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

    public ResponseEntity<?> Update_Comment(PostComment postComment, Integer idPostCom, Integer idUser) {
        if (postCommentRepo.existsById(idPostCom)) {
            PostComment postCom1 = postCommentRepo.findById(idPostCom)
                    .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

            postCom1.setCommentBody(postComment.getCommentBody());
            postCommentRepo.save(postCom1);
            return ResponseEntity.ok().body("comment updated");

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment Not Found");
        }
    }
    public ResponseEntity<?> Delete_PostCom(Integer idPostCom, Integer idUser) {
        if (postCommentRepo.existsById(idPostCom)) {
            PostComment postCom1 = postCommentRepo.findById(idPostCom)
                    .orElseThrow(() -> new EntityNotFoundException("post not found"));
            User user = userRepo.findById(idUser).orElseThrow(() -> new EntityNotFoundException("User not found"));
            if (postCom1.getUser().equals(user)) {
                postCommentRepo.delete(postCom1);
                return ResponseEntity.ok().body("Delete success");
            } else {
                return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("No permission to delete this post");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("post Not Founf");
        }
    }
    public List<PostComment> get_post_Comm(Integer idPost) {

        return postCommentRepo.findByPost(postRepo.getReferenceById(idPost));

    }

    public List<PostComment> get_comm_Comm(Integer idComment) {
        return postCommentRepo.findByPostComment(postCommentRepo.getReferenceById(idComment));
    }







    //Advertising
    public ResponseEntity<?> addAdvertising(Advertising a , Integer idCategory ,List<MultipartFile> files,  Integer IdUser) throws IOException {
        CategoryAdve c =  categoryAdvrepo.findById(idCategory).orElse(null);
        User u = userRepo.findById(IdUser).orElse(null);
        a.setCategoryadv(c);
        if (Filtrage_bad_word(a.getName()) == 0) {
            a.setUser(u);
            u.getAdvertising().add(a);
            a.setPictures(addimages(files));
            advertisingRepo.save(a);
            return ResponseEntity.ok().body(a);
        } else
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Bads Word Detected");
    }
    public ResponseEntity<?> Update_Adversting(Advertising a, Integer idPost) {
        if (postRepo.existsById(idPost)) {
            Advertising a1 = advertisingRepo.findById(idPost).orElseThrow(() -> new EntityNotFoundException("adversting not found"));
            a1.setName(a.getName());
            a1.setPrice(a.getPrice());
            a1.setEndDate(a.getEndDate());
            a1.setStartDate(a.getStartDate());

            advertisingRepo.saveAndFlush(a1);
            return ResponseEntity.ok().body(a1);

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("adversting Not Founf");
        }
    }
    public ResponseEntity<?> Delete_Adversting(Integer idadv) {
        if (advertisingRepo.existsById(idadv)) {
            Advertising a1 = advertisingRepo.findById(idadv).orElseThrow(() -> new EntityNotFoundException("adv not found"));

            advertisingRepo.delete(a1);
            return ResponseEntity.ok().body("Delete success");

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("adv Not Founf");
        }

    }

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

    // get adversting for uUser with DataLoads && age cible
   /* public List<Advertising> getAdverByUserData(Integer idUser){
        UserDataLoad dataus = new UserDataLoad();
        List<Advertising> ll = new ArrayList<>();
        int x = 0 ;
        for (UserDataLoad data : userDataLoadRepo.findAll()) {

            if (data.getUser().getId() == idUser) {
                if (data.getNbrsRequet()>=x) {
                    x= data.getNbrsRequet();
                    dataus = data;
                }}}
        List<Advertising> aa = advertisingRepo.findAll();
        for (Advertising advertising : aa) {
            if(advertising.getCategoryadv().getNameCategory().equals(dataus.getCategorieData()) && advertising.getMinage()<=getUserAge(idUser) && advertising.getMaxage()>=getuserage(idUser))
                ll.add(advertising);
        }
        return ll;
    }*/


    // ocr add image

    public List<Pictures> addimages(List<MultipartFile> files) throws IOException {
       // String ch = DoOCR(image);
       // BufferedImage bi = ImageIO.read(image.getInputStream());
       // if (Filtrage_bad_word(ch) == 0 ) {
        //    Map result = cloudImage.upload(image);

          //  Media media = new Media((String)
            //        result.get("original_filename")
              //      , (String) result.get("url"),
                //    (String) result.get("public_id"));
           // media.setPost(p);
            //mediaService.save(media);
	/*
	Set<Media> lp = p.getMedias();
	lp.add(media);
	p.setMedias(lp);
	*/
            //postRepo.save(p);

        List<Pictures> picturesList = new ArrayList<>();
        for (MultipartFile file : files) {
            Pictures picture = new Pictures();
            byte[] data = file.getBytes();
            if (data.length > 500) { // check if the file is too large
                data = Arrays.copyOfRange(data, 0, 500); // truncate the data
            }
            picture.setData(data);
            picturesList.add(picture);
        }


            return picturesRepo.saveAll(picturesList);
        //}
       // else return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("U r Image Content interdit word");

    }

    public String DoOCR(
            MultipartFile image) throws IOException {


        OcrModel request = new OcrModel();
        request.setDestinationLanguage("eng");
        request.setImage(image);

        ITesseract instance = new Tesseract();

        try {

            BufferedImage in = ImageIO.read(convert(image));

            BufferedImage newImage = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics2D g = newImage.createGraphics();
            g.drawImage(in, 0, 0, null);
            g.dispose();

            instance.setLanguage(request.getDestinationLanguage());
            instance.setDatapath("..\\WomenEmpowerment\\tessdata");

            String result = instance.doOCR(newImage);

            return result;

        } catch (TesseractException | IOException e) {
            System.err.println(e.getMessage());
            return "Error while reading image";
        }

    }
    public static File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }


}
