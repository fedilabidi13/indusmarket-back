package tn.esprit.usermanagement.servicesImpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.*;
@Service
@AllArgsConstructor
@Slf4j
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
    ProductRepo productRepo;
    @Autowired
    PostRepo postRepo;
    @Autowired
    ShoppingCartImpl shoppingCart;
    @Autowired
    CartItemImpl cartItem;
    @Autowired
    OrderImpl order;
    @Autowired
    CartItemRepo cartItemRepo;
    @Autowired
    EmailService emailService;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    AdminServiceImpl adminService;
    @Autowired
    InvoiceImpl invoiceService;
    @Autowired
    ClaimProductRefRepo claimProductRefRepo;
    @Override
    public List<Claims> ShowAllClaims() {
        return claimsRepo.findAll();
    }

    @Override
    public List<Claims> ShowClaimsByType(TypeClaim typeClaim) {

        return claimsRepo.findByTypeClaim(typeClaim);
    }
    @Override
    public List<Claims> ShowClaimsByUser() {
    User user = authenticationService.currentlyAuthenticatedUser();
    return user.getClaims();
    }

    @Override
    public List<Claims> ShowClaimsByOrder(int orderId) {
        Orders orders = orderRepo.findById(orderId).get();
return orders.getClaims();
    }

    @Override
    public Claims AddClaimsWithPicturesAndAssignToUser( Claims claim, List<MultipartFile> files) throws IOException {
        User user = authenticationService.currentlyAuthenticatedUser();
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
    public String AddDeliveryClaimsToOrderWithPicturesAndAssignToUser(Integer orderId,Claims claim, List<MultipartFile> files) throws IOException {

        User user = authenticationService.currentlyAuthenticatedUser();
        claim.setUser(user);
        claim.setCreatedAt(LocalDateTime.now());
        claim.setConsultAt(null);
        claim.setTypeClaim(TypeClaim.DELIVERY);
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
         return " Your claim"+claim.getIdClaims().toString()+" saved succefully";
    }
    @Override
    public String AddOrdersClaimsToOrderWithPicturesAndAssignToUser(Integer orderId, Claims claim, List<MultipartFile> files) throws IOException {
        User user = authenticationService.currentlyAuthenticatedUser();
        claim.setUser(user);
        claim.setCreatedAt(LocalDateTime.now());
        claim.setConsultAt(null);
        claim.setTypeClaim(TypeClaim.Order);
        claim.setStatusClaims(StatusClaims.Pending);
        Orders orders = orderRepo.findById(orderId).get();
        claim.setOrder(orders);
       claimsRepo.save(claim);
        List<ClaimProductRef> claimProductRefs = claim.getClaimProductRefs();
        for (ClaimProductRef c : claimProductRefs){
            claimProductRefRepo.save(c);
            c.setClaims(claim);
            claimProductRefRepo.save(c);
        }
        claimsRepo.save(claim);
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
        claim.setPictures(picturesList);
        claimsRepo.save(claim);
        return " Your claim"+claim.getIdClaims().toString()+" saved succefully";
    }
    @Override
    public String AddClaimsToPostWithPicturesAndAssignToUser(Integer postId, Claims claim, List<MultipartFile> files) throws IOException {
        User user = authenticationService.currentlyAuthenticatedUser();
        Post post = postRepo.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post not found"));
        // Check if user already has a claim for this post
        List<Claims> userClaims = claimsRepo.findByUserAndPost(user, post);
        if (!userClaims.isEmpty()) {
            Claims existingClaim = userClaims.get(0);
            if (existingClaim.getStatusClaims() == StatusClaims.In_process||existingClaim.getStatusClaims() == StatusClaims.Rejected||existingClaim.getStatusClaims() == StatusClaims.Resolved) {
                // Existing claim is pending, user cannot update it
                return "Cannot update claim, it is pending";
            } else {
                // Existing claim is not pending, update it
                existingClaim.setDescription(claim.getDescription());
                // delete old pictures
                List<Pictures> oldPictures = existingClaim.getPictures();
                if (oldPictures != null) {
                    picturesRepo.deleteAll(oldPictures);
                }
                // set new pictures
                List<Pictures> newPictures = new ArrayList<>();
                for (MultipartFile file : files) {
                    Pictures picture = new Pictures();
                    byte[] data = file.getBytes();
                    if (data.length > 500) { // check if the file is too large
                        data = Arrays.copyOfRange(data, 0, 500); // truncate the data
                    }
                    picture.setContentType(file.getContentType());
                    picture.setData(data);
                    newPictures.add(picture);
                }
                picturesRepo.saveAll(newPictures);
                existingClaim.setPictures(newPictures);
                Claims savedClaim = claimsRepo.save(existingClaim);
                return "Updated existing claim";
            }
        }
        // Create a new claim
        claim.setUser(user);
        claim.setCreatedAt(LocalDateTime.now());
        claim.setConsultAt(null);
        claim.setStatusClaims(StatusClaims.Pending);
        claim.setPost(post);
        claim.setTypeClaim(TypeClaim.Post);
        Claims savedClaim = claimsRepo.save(claim);

        // save pictures
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

        return "Saved new claim";
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
    @Scheduled(fixedRate = 10000) // runs every second
    @Override
    public void DeleteRejectedClaims() {
        claimsRepo.deleteAll(claimsRepo.findByStatusClaimsAndConsultAtIsBefore (StatusClaims.Rejected, LocalDateTime.now().minusMonths(12)));
    }
    @Transactional
    @Override
    public String updateOrderClaims(Integer orderId, Claims claims, List<MultipartFile> files) throws IOException {
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

                    claims.setOrder(orderRepo.findById(orderId).get());
                claimProductRefRepo.saveAll(claims.getClaimProductRefs());
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
    @Transactional
    @Override
    public String updatePostClaims(Integer postId, Claims claims, List<MultipartFile> files) throws IOException {
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
                claims.setStatusClaims(StatusClaims.Pending);
                claims.setPost(postRepo.findById(postId).get());
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
    @Transactional
    @Override
    public String updateDeliveryClaims(Integer orderId, Claims claims, List<MultipartFile> files) throws IOException {
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
               claims.setOrder(orderRepo.findById(orderId).get());
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

    @Override
    public String OrderClaimTreatment(Integer claimId, StatusClaims status) {
        if (claimsRepo.findById(claimId).get().getStatusClaims()==StatusClaims.Resolved||claimsRepo.findById(claimId).get().getStatusClaims()==StatusClaims.Rejected){
            return "claims was treated before !";
        } else if (claimsRepo.findById(claimId).get().getStatusClaims()==StatusClaims.Pending) {
            return "You can't treat right now !";
        }
        else {
            if (status == StatusClaims.Resolved && claimsRepo.findById(claimId).get().getTypeClaim() == TypeClaim.Order) {
                List<ClaimProductRef> claimProductRefs = claimsRepo.findById(claimId).get().getClaimProductRefs();
                float totalAmount = 0;
                for (ClaimProductRef claimProductRef : claimProductRefs) {
                    String productReference = claimProductRef.getProductRef();
                    Integer quantity = claimProductRef.getQuantity();
                    Product product = productRepo.findByReference(productReference);
                    if (product == null) {
                        throw new RuntimeException("Product not found");
                    }
                    float price = product.getPriceAfterDiscount() > 0 ? product.getPriceAfterDiscount() : product.getPrice();
                    totalAmount += price * quantity;
                    CartItem cartItem = cartItemRepo.findAllByProductReference(productReference);
                    if (cartItem == null) {
                        throw new RuntimeException("Cart item not found");
                    }
                    Shop shop = product.getShop();
                    User shopUser = shop.getUser();
                    shopUser.setBanNumber(shopUser.getBanNumber() + 1);
                    if (shopUser.getBanNumber() >= 5) {
                        adminService.banUser(shopUser.getEmail());
                    } else {
                        adminService.suspendUser(shopUser.getEmail());
                    }
                }
                Orders newOrder = new Orders();
                newOrder.setCreationDate(LocalDateTime.now());
                newOrder.setPaid(true);
                newOrder.setTotalAmount(totalAmount);
                newOrder.setDeliveryS(claimsRepo.findById(claimId).get().getOrder().getDeliveryS());
                newOrder.setUser(claimsRepo.findById(claimId).get().getOrder().getUser());
                newOrder.setDilevryAdresse(claimsRepo.findById(claimId).get().getOrder().getDilevryAdresse());
                orderRepo.save(newOrder);
            } else if (status == StatusClaims.Rejected) {
                emailService.sendClaimEmail(claimsRepo.findById(claimId).get().getUser().getEmail(), "Your claim was rejected");
            }
            claimsRepo.findById(claimId).get().setStatusClaims(status);
            claimsRepo.findById(claimId).get().setConsultAt(LocalDateTime.now());
            claimsRepo.save(claimsRepo.findById(claimId).get());
            return "claims treated succefully !";
        }
    }
    @Override
    public String PostClaimTreatment(Integer claimId, StatusClaims status) {
        if (claimsRepo.findById(claimId).get().getStatusClaims()==StatusClaims.Resolved||claimsRepo.findById(claimId).get().getStatusClaims()==StatusClaims.Rejected){
            return "claims was treated before !";
        } else if (claimsRepo.findById(claimId).get().getStatusClaims()==StatusClaims.Pending) {
            return "You can't treat right now !";
        }
        else {
            if (status == StatusClaims.Resolved) {
                if (claimsRepo.findById(claimId).get().getTypeClaim() == TypeClaim.Post) {
                    long numResolvedClaims = claimsRepo.findById(claimId).get().getPost().getClaims().stream()
                            .filter(c -> c.getStatusClaims() == StatusClaims.Resolved)
                            .count();
                    if (numResolvedClaims >= 2) {
                        postRepo.delete(claimsRepo.findById(claimId).get().getPost());
                    } else {
                        emailService.sendClaimEmail(claimsRepo.findById(claimId).get().getPost().getUser().getEmail(), "there is a claim about your post: " + claimsRepo.findById(claimId).get().getPost().getBody() + " so you should modify it!");
                    }
                    long numClaimedPosts = claimsRepo.findById(claimId).get().getPost().getUser().getPosts().stream()
                            .filter(p -> p.getClaims() != null)
                            .count();
                    if (numClaimedPosts >= 2) {
                        adminService.suspendUser(claimsRepo.findById(claimId).get().getPost().getUser().getEmail());
                    }
                    if (claimsRepo.findById(claimId).get().getPost().getUser().getBanNumber() >= 5) {
                        adminService.banUser(claimsRepo.findById(claimId).get().getPost().getUser().getEmail());
                    }
                }
            } else {
                emailService.sendClaimEmail(claimsRepo.findById(claimId).get().getUser().getEmail(), "your claim was rejected");
            }
            claimsRepo.findById(claimId).get().setStatusClaims(status);
            claimsRepo.findById(claimId).get().setConsultAt(LocalDateTime.now());
            claimsRepo.save(claimsRepo.findById(claimId).get());
            return "claims treated succefully !";
        }
    }
    @Override
    public String DeliviryClaimTreatment(Integer claimId, StatusClaims status) {
        if (claimsRepo.findById(claimId).get().getStatusClaims()==StatusClaims.Resolved||claimsRepo.findById(claimId).get().getStatusClaims()==StatusClaims.Rejected){
            return "claims was treated before !";
        }else if (claimsRepo.findById(claimId).get().getStatusClaims()==StatusClaims.Pending) {
            return "You can't treat right now !";
        }
        else {
            if (status == StatusClaims.Resolved) {
                if (claimsRepo.findById(claimId).get().getTypeClaim() == TypeClaim.DELIVERY) {
                    adminService.suspendUser(claimsRepo.findById(claimId).get().getOrder().getDeliveryS().getLivreur().getEmail());
                    emailService.sendClaimEmail(claimsRepo.findById(claimId).get().getOrder().getDeliveryS().getLivreur().getEmail(), "there is a claim about your service: " + claimsRepo.findById(claimId).get().getDescription() + "So you are banned for a week ");
                    if (claimsRepo.findById(claimId).get().getOrder().getDeliveryS().getLivreur().getBanNumber() >= 7) {
                        adminService.banUser(claimsRepo.findById(claimId).get().getOrder().getDeliveryS().getLivreur().getEmail());
                        emailService.sendClaimEmail(claimsRepo.findById(claimId).get().getOrder().getDeliveryS().getLivreur().getEmail(), "You are claimed 7 times So you are banned ");
                    }
                }
                emailService.sendClaimEmail(claimsRepo.findById(claimId).get().getUser().getEmail(), "your claim was resolved we will see what shoul as do about " + claimsRepo.findById(claimId).get().getDescription());
            } else {
                emailService.sendClaimEmail(claimsRepo.findById(claimId).get().getUser().getEmail(), "your claim was rejected");
            }
            claimsRepo.findById(claimId).get().setStatusClaims(status);
            claimsRepo.findById(claimId).get().setStatusClaims(status);
            claimsRepo.save(claimsRepo.findById(claimId).get());
            return "claims treated succefully !";
        }
    }
    @Override
    public String OtherClaimTreatment(Integer claimId, StatusClaims status) {
        if (claimsRepo.findById(claimId).get().getStatusClaims()==StatusClaims.Resolved||claimsRepo.findById(claimId).get().getStatusClaims()==StatusClaims.Rejected){
            return "claims was treated before !";
        } else if (claimsRepo.findById(claimId).get().getStatusClaims()==StatusClaims.Pending) {
            return "You can't treat right now !";
        }
        else {
        if (status == StatusClaims.Resolved || claimsRepo.findById(claimId).get().getTypeClaim()==TypeClaim.Other) {

            emailService.sendClaimEmail(claimsRepo.findById(claimId).get().getUser().getEmail(), "your claim was resolved we will see what shoul as do about " + claimsRepo.findById(claimId).get().getDescription());
        }
        else {
            emailService.sendClaimEmail(claimsRepo.findById(claimId).get().getUser().getEmail(), "your claim was rejected");
        }
        claimsRepo.findById(claimId).get().setStatusClaims(status);
        claimsRepo.findById(claimId).get().setConsultAt(LocalDateTime.now());
        claimsRepo.save(claimsRepo.findById(claimId).get());
        return "claims treated succefully !";
        }
    }
}

