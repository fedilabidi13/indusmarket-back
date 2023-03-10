package tn.esprit.usermanagement.servicesImpl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.*;
import tn.esprit.usermanagement.entities.ForumEntities.Post;
import tn.esprit.usermanagement.enumerations.StatusClaims;
import tn.esprit.usermanagement.enumerations.TypeClaim;
import tn.esprit.usermanagement.repositories.*;
import tn.esprit.usermanagement.services.ClaimsService;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClaimsServiceImpl implements ClaimsService {
    @Autowired
    ClaimsRepo claimsRepo;
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    PicturesRepo picturesRepo;
    @Autowired
    PostRepo postRepo;
    @Autowired
    ShoppingCartImpl shoppingCart;
    @Autowired
    CartItemImpl cartItem;
    @Autowired
    OrderImpl order;
    @Autowired
    EmailService emailService;
    @Override
    public Claims addClaims(Claims claims) {
        return claimsRepo.save(claims);
    }



    @Override
    public List<Claims> ShowAllClaims() {
        return claimsRepo.findAll();
    }

    @Override
    public List<Claims> ShowClaimsByType(TypeClaim typeClaim) {

        return claimsRepo.findByTypeClaim(typeClaim);
    }

    @Override
    public List<Claims> ShowClaimsByUser(int UserId) {
    User user = userRepo.findById(UserId).get();
    return user.getClaims();
    }

    @Override
    public List<Claims> ShowClaimsByOrder(int orderId) {
        Orders orders = orderRepo.findById(orderId).get();
return new ArrayList<>();    }


    @Override
    public Claims AddClaimsWithPicturesAndAssignToUser(Integer userId, Claims claim, List<MultipartFile> files) throws IOException {
        User user = userRepo.findById(userId).get();
        claim.setCreatedAt(LocalDateTime.now());
        claim.setConsultAt(null);
        claim.setStatusClaims(StatusClaims.Pending);
        claim.setUser(user);
        claim.setTypeClaim(TypeClaim.Other);
        Claims savedClaim = claimsRepo.save(claim);
        List<Pictures> picturesList = new ArrayList<>();
        for (MultipartFile file : files) {
            Pictures picture = new Pictures();
            byte[] data = file.getBytes();
            if (data.length > 500) { // check if the file is too large
                data = Arrays.copyOfRange(data, 0, 500); // truncate the data
            }
            picture.setContentType(file.getContentType());
            picture.setData(data);
            picturesList.add(picture);
        }
        picturesRepo.saveAll(picturesList);
        savedClaim.setPictures(picturesList);
        return claimsRepo.save(savedClaim);  }

    @Override
    public String AddClaimsToOrderWithPicturesAndAssignToUser(Integer orderId, Integer userId, Claims claim, List<MultipartFile> files,TypeClaim typeClaim) throws IOException {
        if (typeClaim==TypeClaim.Post||typeClaim==TypeClaim.Other){
            return "Type Claim must me Product or Delivery";
        }
        User user = userRepo.findById(userId).get();
        claim.setUser(user);
        claim.setCreatedAt(LocalDateTime.now());
        claim.setConsultAt(null);
        claim.setTypeClaim(typeClaim);
        claim.setStatusClaims(StatusClaims.Pending);
        Orders orders = orderRepo.findById(orderId).get();
        claim.setOrder(orders);
        Claims savedClaim = claimsRepo.save(claim);
        List<Pictures> picturesList = new ArrayList<>();
        for (MultipartFile file : files) {
            Pictures picture = new Pictures();
            byte[] data = file.getBytes();
            if (data.length > 500) { // check if the file is too large
                data = Arrays.copyOfRange(data, 0, 500); // truncate the data
            }
            picture.setContentType(file.getContentType());
            picture.setData(data);
            picturesList.add(picture);
        }
        picturesRepo.saveAll(picturesList);
        savedClaim.setPictures(picturesList);
         claimsRepo.save(savedClaim);
         return " Your claim saved succefully";
    }

    @Override
    public Claims AddClaimsToPostWithPicturesAndAssignToUser(Integer postId, Integer userId, Claims claim, List<MultipartFile> files) throws IOException {
        User user = userRepo.findById(userId).get();
        claim.setUser(user);
        claim.setCreatedAt(LocalDateTime.now());
        claim.setConsultAt(null);
        claim.setStatusClaims(StatusClaims.Pending);
        Post post = postRepo.findById(postId).get();
        claim.setPost(post);
        claim.setTypeClaim(TypeClaim.Post);
        Claims savedClaim = claimsRepo.save(claim);
        List<Pictures> picturesList = new ArrayList<>();
        for (MultipartFile file : files) {
            Pictures picture = new Pictures();
            byte[] data = file.getBytes();
            if (data.length > 500) { // check if the file is too large
                data = Arrays.copyOfRange(data, 0, 500); // truncate the data
            }
            picture.setContentType(file.getContentType());
            picture.setData(data);
            picturesList.add(picture);
        }
        picturesRepo.saveAll(picturesList);
        savedClaim.setPictures(picturesList);
        return claimsRepo.save(savedClaim);
    }

    @Scheduled(fixedRate = 10000) // runs every second
    @Override
    public void UpdatePendingClaims() {
        LocalDateTime limite = LocalDateTime.now().minusHours(24);
        List<Claims> PendingClaims = claimsRepo.findByStatusClaimsAndCreatedAtIsBefore (StatusClaims.Pending, limite);
        for (Claims claims : PendingClaims) {
            claims.setStatusClaims(StatusClaims.In_process);
            claimsRepo.save(claims);
        }
    }

    @Override
    public void Claimtreatment(Integer ClaimId,StatusClaims status) {
        Claims claims=claimsRepo.findById(ClaimId).get();
        String Email = claims.getUser().getEmail();
        claims.setConsultAt(LocalDateTime.now());
        if (status==StatusClaims.Resolved){
            claims.setStatusClaims(status);
            claimsRepo.save(claims);
            if (claims.getTypeClaim()==TypeClaim.Post){
                Post post = claims.getPost();
                postRepo.delete(post);
            } else if (claims.getTypeClaim()==TypeClaim.Order) {
                Orders orders = claims.getOrder();
                Orders orders1 = new Orders();
                orders1.setDeliveryS(orders.getDeliveryS());
                orders1.setUser(orders.getUser());
                orders1.setTotalAmount(orders.getTotalAmount());
                orders1.setDeliveryS(orders.getDeliveryS());
                orders1.setPaid(true);
                orderRepo.save(orders1);
            } else if (claims.getTypeClaim()==TypeClaim.DELIVERY) {
                Orders orders = claims.getOrder();
                String mail = orders.getDeliveryS().getLivreur().getEmail();
             emailService.send(mail,"there is a claim about your service :  "+claims.getDescription());
            }
            emailService.send(Email,"your claim was resolved ");
            claimsRepo.save(claims);
        }
        else {
            claims.setStatusClaims(status);
            emailService.send(Email,"your claim was rejected ");
        }
        claimsRepo.save(claims);
    }

    @Scheduled(fixedRate = 10000) // runs every second
    @Override
    public void DeleteRejectedClaims() {
        claimsRepo.deleteAll(claimsRepo.findByStatusClaimsAndConsultAtIsBefore (StatusClaims.Rejected, LocalDateTime.now().minusMonths(12)));

    }
    @Transactional
    @Override
    public String UpdateClaims(Integer orderId, Claims claims, List<MultipartFile> files) throws IOException {
        Optional<Claims> optionalClaims = claimsRepo.findById(claims.getIdClaims());
        if (optionalClaims.isPresent()) {
            Claims existingClaims = optionalClaims.get();
            if (existingClaims.getStatusClaims() == StatusClaims.Pending) {
                if (files == null || files.isEmpty()) {
                    claims.setPictures(existingClaims.getPictures());
                } else {
                    List<Pictures> picturesList = new ArrayList<>();
                    for (MultipartFile file : files) {
                        Pictures picture = new Pictures();
                        byte[] data = file.getBytes();
                        if (data.length > 500) {
                            data = Arrays.copyOfRange(data, 0, 500);
                        }
                        picture.setData(data);
                        picture.setContentType(file.getContentType());
                        picturesList.add(picture);
                    }
                    List<Pictures> existingPictures = existingClaims.getPictures();
                    for (Pictures picture : existingPictures) {
                        picturesRepo.delete(picture);
                    }
                    picturesRepo.saveAll(picturesList);
                    claims.setPictures(picturesList);
                }
                if (claims.getTitle() == null) {
                    claims.setTitle(existingClaims.getTitle());
                }
                if (claims.getDescription() == null) {
                    claims.setDescription(existingClaims.getDescription());
                }
                if (claims.getTypeClaim() == null) {
                    claims.setTypeClaim(existingClaims.getTypeClaim());
                }
                if (orderId == null) {
                    claims.setOrder(existingClaims.getOrder());
                } else {
                    claims.setOrder(orderRepo.findById(orderId).get());
                }
                claims.setCreatedAt(existingClaims.getCreatedAt());
                claims.setUser(existingClaims.getUser());
                claims.setStatusClaims(existingClaims.getStatusClaims());
                claimsRepo.save(claims);
                return "Claims updated successfully";
            } else {
                return "You can't update this claim as it is not in Pending status";
            }
        } else {
            return "Claim with id " + claims.getIdClaims() + " not found";
        }
    }


}
