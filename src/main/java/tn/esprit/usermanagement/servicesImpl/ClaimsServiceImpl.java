package tn.esprit.usermanagement.servicesImpl;

import com.cloudinary.Cloudinary;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.*;
import tn.esprit.usermanagement.entities.ForumEntities.Media;
import tn.esprit.usermanagement.entities.ForumEntities.Post;
import tn.esprit.usermanagement.enumerations.Role;
import tn.esprit.usermanagement.enumerations.StatusClaims;
import tn.esprit.usermanagement.enumerations.TypeClaim;
import tn.esprit.usermanagement.repositories.*;
import tn.esprit.usermanagement.services.ClaimsService;
import tn.esprit.usermanagement.servicesImpl.ForumServiceImpl.PostServiceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
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
    @Autowired
    Cloudinary cloudinary;
    @Autowired
    MediaRepo mediaRepo;
    @Autowired
    PostServiceImpl postService;
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
    return claimsRepo.findByUser(user);
    }

    @Override
    public List<Claims> ShowClaimsByOrder(int orderId) {
return claimsRepo.findByOrder(orderRepo.findById(orderId).get());
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
        if (files==null||files.isEmpty()){
            savedClaim.setMedias(null);
            return claimsRepo.save(savedClaim);
        }
        else {
            List<Media> mediaList = new ArrayList<>();
            for (MultipartFile multipartFile : files) {
                Media media = new Media();
                String url = cloudinary.uploader()
                        .upload(multipartFile.getBytes(),
                                Map.of("public_id", UUID.randomUUID().toString()))
                        .get("url")
                        .toString();
                media.setImagenUrl(url);
                media.setName(multipartFile.getName());
                mediaList.add(media);
            }
            mediaRepo.saveAll(mediaList);
            savedClaim.setMedias(mediaList);
        }
        return claimsRepo.save(savedClaim);
    }
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
        List<Media> mediaList = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            Media media = new Media();
            String url = cloudinary.uploader()
                    .upload(multipartFile.getBytes(),
                            Map.of("public_id", UUID.randomUUID().toString()))
                    .get("url")
                    .toString();
            media.setImagenUrl(url);
            media.setName(multipartFile.getName());
            mediaList.add(media);
        }
        mediaRepo.saveAll(mediaList);
        savedClaim.setMedias(mediaList);
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
        List<Media> mediaList = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            Media media = new Media();
            String url = cloudinary.uploader()
                    .upload(multipartFile.getBytes(),
                            Map.of("public_id", UUID.randomUUID().toString()))
                    .get("url")
                    .toString();
            media.setImagenUrl(url);
            media.setName(multipartFile.getName());
            mediaList.add(media);
        }
        mediaRepo.saveAll(mediaList);
        claim.setMedias(mediaList);
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
                List<Media> oldPictures = existingClaim.getMedias();
                if (oldPictures != null) {
                    mediaRepo.deleteAll(oldPictures);
                }
                // set new pictures
                List<Media> mediaList = new ArrayList<>();
                for (MultipartFile multipartFile : files) {
                    Media media = new Media();
                    String url = cloudinary.uploader()
                            .upload(multipartFile.getBytes(),
                                    Map.of("public_id", UUID.randomUUID().toString()))
                            .get("url")
                            .toString();
                    media.setImagenUrl(url);
                    media.setName(multipartFile.getName());
                    mediaList.add(media);
                }
                mediaRepo.saveAll(mediaList);
                existingClaim.setMedias(mediaList);
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
        List<Media> mediaList = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            Media media = new Media();
            String url = cloudinary.uploader()
                    .upload(multipartFile.getBytes(),
                            Map.of("public_id", UUID.randomUUID().toString()))
                    .get("url")
                    .toString();
            media.setImagenUrl(url);
            media.setName(multipartFile.getName());
            mediaList.add(media);
        }
        mediaRepo.saveAll(mediaList);
        savedClaim.setMedias(mediaList);
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
            claimProductRefRepo.deleteAll(existingClaims.getClaimProductRefs());
            if (existingClaims.getStatusClaims() == StatusClaims.Pending) {
                if (files == null || files.isEmpty()) {
                    claims.setMedias(existingClaims.getMedias());
                } else {
                    List<Media> mediaList = new ArrayList<>();
                    for (MultipartFile multipartFile : files) {
                        Media media = new Media();
                        String url = cloudinary.uploader()
                                .upload(multipartFile.getBytes(),
                                        Map.of("public_id", UUID.randomUUID().toString()))
                                .get("url")
                                .toString();
                        media.setImagenUrl(url);
                        media.setName(multipartFile.getName());
                        mediaList.add(media);
                    }
                    mediaRepo.saveAll(mediaList);
                    List<Media> existingPictures = existingClaims.getMedias();
                    for (Media picture : existingPictures) {
                        mediaRepo.delete(picture);
                    }
                    mediaRepo.saveAll(mediaList);
                    claims.setMedias(mediaList);
                }
                claims.setOrder(orderRepo.findById(orderId).get());
                List<ClaimProductRef> claimProductRefs = claims.getClaimProductRefs();
                for (ClaimProductRef c : claimProductRefs){
                    claimProductRefRepo.save(c);
                    c.setClaims(claims);
                    claimProductRefRepo.save(c);
                }
                claimsRepo.save(claims);
                claimProductRefRepo.saveAll(claims.getClaimProductRefs());
                claims.setCreatedAt(existingClaims.getCreatedAt());
                claims.setStatusClaims(existingClaims.getStatusClaims());
                claims.setUser(existingClaims.getUser());
                claims.setTypeClaim(TypeClaim.Order);
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
                    claims.setMedias(existingClaims.getMedias());
                } else {
                    List<Media> mediaList = new ArrayList<>();
                    for (MultipartFile multipartFile : files) {
                        Media media = new Media();
                        String url = cloudinary.uploader()
                                .upload(multipartFile.getBytes(),
                                        Map.of("public_id", UUID.randomUUID().toString()))
                                .get("url")
                                .toString();
                        media.setImagenUrl(url);
                        media.setName(multipartFile.getName());
                        mediaList.add(media);
                    }
                    List<Media> existingPictures = existingClaims.getMedias();
                    for (Media picture : existingPictures) {
                        mediaRepo.delete(picture);
                    }
                    mediaRepo.saveAll(mediaList);
                    claims.setMedias(mediaList);
                }
               claims.setOrder(orderRepo.findById(orderId).get());
                claims.setCreatedAt(existingClaims.getCreatedAt());
                claims.setUser(existingClaims.getUser());
                claims.setStatusClaims(existingClaims.getStatusClaims());
                claims.setTypeClaim(TypeClaim.DELIVERY);
                claims.setClaimProductRefs(null);
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
    public ResponseEntity<Object> OrderClaimTreatment(Integer claimId, StatusClaims status) {
        User usr = authenticationService.currentlyAuthenticatedUser();
        if (usr.getRole() != Role.MOD) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("You are not authorized to perform this action");
        }
        Claims claims = claimsRepo.findById(claimId).get();
        if (claims.getStatusClaims()==StatusClaims.Resolved||claims.getStatusClaims()==StatusClaims.Rejected){
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Seen");
        } else if (claims.getStatusClaims()==StatusClaims.Pending) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("You can't");
        }
        else {
            if (status == StatusClaims.Resolved && claims.getTypeClaim() == TypeClaim.Order) {
                List<ClaimProductRef> claimProductRefs = claims.getClaimProductRefs();
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
                newOrder.setDeliveryS(claims.getOrder().getDeliveryS());
                newOrder.setUser(claims.getOrder().getUser());
                newOrder.setDilevryAdresse(claims.getOrder().getDilevryAdresse());
                orderRepo.save(newOrder);
            } else if (status == StatusClaims.Rejected) {
                emailService.sendClaimEmail(claims.getUser().getEmail(), "Your claim was rejected");
            }
            claims.setStatusClaims(status);
            claims.setConsultAt(LocalDateTime.now());
            claimsRepo.save(claims);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Claim has been successfully treated\"}");
        }
    }
    @Transactional
    @Override
    public ResponseEntity<Object> PostClaimTreatment(Integer claimId, StatusClaims status) {
        User usr = authenticationService.currentlyAuthenticatedUser();
        if (usr.getRole() != Role.MOD) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("You are not authorized to perform this action");
        }
        Claims claims = claimsRepo.findById(claimId).get();
        if (claims.getStatusClaims() == StatusClaims.Resolved || claims.getStatusClaims() == StatusClaims.Rejected) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Seen");
        } else if (claims.getStatusClaims() == StatusClaims.Pending) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("You can't");
        } else {
            if (status == StatusClaims.Resolved) {
                if (claims.getTypeClaim() == TypeClaim.Post) {
                    long numResolvedClaims = claims.getPost().getClaims().stream()
                            .filter(c -> c.getStatusClaims() == StatusClaims.Resolved)
                            .count();
                    if (numResolvedClaims >= 50) {
                        postRepo.delete(claims.getPost());
                    } else {
                        emailService.sendClaimEmail(claims.getPost().getUser().getEmail(), "there is a claim about your post: " + claims.getPost().getBody() + " so you should modify it!");
                    }
                    long numClaimedPosts = claims.getPost().getUser().getPosts().stream()
                            .filter(p -> p.getClaims() != null)
                            .count();
                    if (numClaimedPosts >7) {
                        adminService.suspendUser(claims.getPost().getUser().getEmail());
                    }
                    if (claims.getPost().getUser().getBanNumber() >= 10) {
                        adminService.banUser(claims.getPost().getUser().getEmail());
                    }
                }
            } else {
                emailService.sendClaimEmail(claims.getUser().getEmail(), "your claim was rejected");
            }
            claims.setStatusClaims(status);
            claims.setConsultAt(LocalDateTime.now());
            claimsRepo.save(claims);
            return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Claim has been successfully treated\"}");
        }
    }

    @Override
    public ResponseEntity<Object> DeliviryClaimTreatment(Integer claimId, StatusClaims status) {
        User usr = authenticationService.currentlyAuthenticatedUser();
        if (usr.getRole() != Role.MOD) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("You are not authorized to perform this action");
        }
        Claims claims = claimsRepo.findById(claimId).orElse(null);
        if (claims == null) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Invalid Claim Id");
        }
        if (claims.getStatusClaims() != StatusClaims.In_process) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Seen");
        }
        if (status == StatusClaims.Resolved) {
            if (claims.getTypeClaim() == TypeClaim.DELIVERY) {
                Delivery delivery = claims.getOrder().getDeliveryS();
                if (delivery != null) {
                    User livreur = delivery.getLivreur();
                    if (livreur != null) {
                        adminService.suspendUser(livreur.getEmail());
                        emailService.sendClaimEmail(livreur.getEmail(), "There is a claim about your service: " + claims.getDescription() + ". You are banned for a week.");
                        if (livreur.getBanNumber() >= 2) {
                            adminService.banUser(livreur.getEmail());
                            emailService.sendClaimEmail(livreur.getEmail(), "You have been claimed 7 times, so you are now permanently banned.");
                        }
                    }
                }
            }
            emailService.sendClaimEmail(claims.getUser().getEmail(), "Your claim was resolved. We will see what should be done about " + claims.getDescription());
        } else {
            emailService.sendClaimEmail(claims.getUser().getEmail(), "Your claim was rejected.");
        }

        claims.setStatusClaims(status);
        claims.setConsultAt(LocalDateTime.now());
        claimsRepo.save(claims);

        return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Claim has been successfully treated\"}");
    }

    @Override
    public ResponseEntity<Object> OtherClaimTreatment(Integer claimId, StatusClaims status) {
        User usr = authenticationService.currentlyAuthenticatedUser();
        if (usr.getRole() != Role.MOD) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("You are not authorized to perform this action");
        }
        Claims claims = claimsRepo.findById(claimId).orElse(null);
        if (claims == null) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Invalid Claim Id");
        }
        if (claims.getStatusClaims() != StatusClaims.In_process) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Claim has alredy seen");

        }
        if (status != StatusClaims.Rejected && claims.getTypeClaim() != TypeClaim.Other) {
            emailService.sendClaimEmail(claims.getUser().getEmail(), "Your claim was rejected.");
        } else {
            emailService.sendClaimEmail(claims.getUser().getEmail(), "Your claim was resolved. We will see what should be done about " + claims.getDescription());
        }
        claims.setConsultAt(LocalDateTime.now());
        claims.setStatusClaims(status);
        claimsRepo.save(claims);
        return ResponseEntity.status(HttpStatus.OK).body("{\"message\": \"Claim has been successfully treated\"}");
    }
    @Override
    public ResponseEntity<Object> claimTreatment(Integer claimId, StatusClaims status) {
        Claims claims = claimsRepo.findById(claimId).orElse(null);
        if (claims == null) {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Claim not found");
        }

        TypeClaim typeClaim = claims.getTypeClaim();

        if (typeClaim == TypeClaim.Order) {
            return OrderClaimTreatment(claimId, status);
        } else if (typeClaim == TypeClaim.Post) {
            return PostClaimTreatment(claimId, status);
        } else if(typeClaim == TypeClaim.DELIVERY) {
            return DeliviryClaimTreatment(claimId,status) ;
        } else if (typeClaim==TypeClaim.Other) {
            return OtherClaimTreatment(claimId,status);
        }
        else {
            return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("Invalid type claim");
        }
    }

    @Override
    public void DeleteClaim(Integer id) {
        claimsRepo.deleteById(id);
    }

}

