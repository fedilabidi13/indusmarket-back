package tn.esprit.usermanagement.servicesImpl;

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
         claimsRepo.save(savedClaim);
         return "saved";
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
    public void OrderClaimTreatment(Integer claimId, StatusClaims status) {
        Claims claim = claimsRepo.findById(claimId).orElseThrow(() -> new RuntimeException("Claim not found"));
        claim.setStatusClaims(status);
        if (status == StatusClaims.Resolved && claim.getTypeClaim() == TypeClaim.Order) {
            List<ClaimProductRef> claimProductRefs = claim.getClaimProductRefs();
            float totalAmount = 0;
            List<CartItem> cartItems = new ArrayList<>();
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
                cartItems.add(cartItem);
                cartItemRepo.saveAll(cartItems);
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
            newOrder.setDeliveryS(claim.getOrder().getDeliveryS());
            newOrder.setUser(claim.getOrder().getUser());
            newOrder.setDilevryAdresse(claim.getOrder().getDilevryAdresse());
            orderRepo.save(newOrder);
        } else if (status == StatusClaims.Rejected) {
            emailService.sendClaimEmail(claim.getUser().getEmail(), "Your claim was rejected");
        }
        claimsRepo.save(claim);
    }
    @Override
    public void PostClaimTreatment(Integer claimId, StatusClaims status) {
        Claims claims = claimsRepo.findById(claimId).get();
        claims.setStatusClaims(status);
        if (status == StatusClaims.Resolved) {
            if (claims.getTypeClaim() == TypeClaim.Post) {
                Post post = claims.getPost();
                long numResolvedClaims = post.getClaims().stream()
                        .filter(c -> c.getStatusClaims() == StatusClaims.Resolved)
                        .count();
                if (numResolvedClaims >= 2) {
                    postRepo.delete(post);
                } else {
                    emailService.sendClaimEmail(post.getUser().getEmail(), "there is a claim about your post: " + post.getBody() + " so you should modify it!");
                }
                long numClaimedPosts = post.getUser().getPosts().stream()
                        .filter(p -> p.getClaims() != null)
                        .count();
                if (numClaimedPosts >= 2) {
                    adminService.suspendUser(post.getUser().getEmail());
                }
                if (post.getUser().getBanNumber() >= 5) {
                    adminService.banUser(post.getUser().getEmail());
                }
            }
        }
        else {
            emailService.sendClaimEmail(claims.getUser().getEmail(), "your claim was rejected");
        }
        claimsRepo.save(claims);
    }
    @Override
    public void DeliviryClaimTreatment(Integer claimId, StatusClaims status) {
        Claims claims = claimsRepo.findById(claimId).get();
        claims.setStatusClaims(status);
        if (status == StatusClaims.Resolved) {
        if (claims.getTypeClaim() == TypeClaim.DELIVERY) {
            Orders orders = claims.getOrder();
            adminService.suspendUser(orders.getDeliveryS().getLivreur().getEmail());
            emailService.sendClaimEmail(orders.getDeliveryS().getLivreur().getEmail(), "there is a claim about your service: " + claims.getDescription()+"So you are banned for a week ");
            if (orders.getDeliveryS().getLivreur().getBanNumber()>=7){
                adminService.banUser(orders.getDeliveryS().getLivreur().getEmail());
                emailService.sendClaimEmail(orders.getDeliveryS().getLivreur().getEmail(), "You are claimed 7 times So you are banned ");
            }
        }
        emailService.sendClaimEmail(claims.getUser().getEmail(), "your claim was resolved we will see what shoul as do about " + claims.getDescription());
        }
        else {
        emailService.sendClaimEmail(claims.getUser().getEmail(), "your claim was rejected");
        }
        claimsRepo.save(claims);
    }
    @Override
    public void OtherClaimTreatment(Integer claimId, StatusClaims status) {
        Claims claims = claimsRepo.findById(claimId).get();
        claims.setStatusClaims(status);
        if (status == StatusClaims.Resolved) {

            emailService.sendClaimEmail(claims.getUser().getEmail(), "your claim was resolved we will see what shoul as do about " + claims.getDescription());
        }
        else {
            emailService.sendClaimEmail(claims.getUser().getEmail(), "your claim was rejected");
        }
        claimsRepo.save(claims);
    }
     /*
    @Override
    public void claimTreatment(Integer claimId, StatusClaims status) {
        float amount=0;
        Claims claims = claimsRepo.findById(claimId).orElseThrow();
        String email = claims.getUser().getEmail();
        claims.setConsultAt(LocalDateTime.now());
        claims.setStatusClaims(status);
        if (status == StatusClaims.Resolved) {
            if (claims.getTypeClaim() == TypeClaim.Post) {
                Post post = claims.getPost();
                long numResolvedClaims = post.getClaims().stream()
                        .filter(c -> c.getStatusClaims() == StatusClaims.Resolved)
                        .count();
                if (numResolvedClaims >= 50) {
                    postRepo.delete(post);
                } else {
                    emailService.sendClaimEmail(email, "there is a claim about your post: " + post.getBody() + " so you should modify it!");
                }
                long numClaimedPosts = post.getUser().getPosts().stream()
                        .filter(p -> p.getClaims() != null)
                        .count();
                if (numClaimedPosts >= 10) {
                    adminService.suspendUser(post.getUser().getEmail());
                }
                if (post.getUser().getBanNumber()>=7){
                    adminService.banUser(post.getUser().getEmail());
                }
            } else if (claims.getTypeClaim() == TypeClaim.Order) {
                Orders orders = claims.getOrder();
                List<CartItem> cartItemList = orders.getSecondCartItemList();
                List<ClaimProductRef> claimProductRefs = claims.getClaimProductRefs();
                List<CartItem> cartItemList1 = new ArrayList<>();
                for (ClaimProductRef cp : claimProductRefs) {
                    CartItem cartItem1 = cartItemRepo.findAllByProductReference(cp.getProductRef());
                    cartItemList1.add(cartItem1);
                   if (cartItemList.contains(cartItemList1)) {
                       Product p = productRepo.findByReference(cp.getProductRef());
                       Integer qt = cp.getQuantity();
                       if (p.getPriceAfterDiscount() == 0) {
                           amount += p.getPrice() * qt;
                       } else {
                           amount += p.getPriceAfterDiscount() * qt;
                       }
                       adminService.suspendUser(p.getShop().getUser().getEmail());
                       if (p.getShop().getUser().getBanNumber() >= 5) {
                           adminService.banUser(p.getShop().getUser().getEmail());
                       }
                   }
                }
                Orders newOrder = new Orders();
                newOrder.setDeliveryS(orders.getDeliveryS());
                newOrder.setPaid(true);
                newOrder.setUser(orders.getUser());
                newOrder.setCreationDate(LocalDateTime.now());
                newOrder.setTotalAmount(amount);
                newOrder.setDilevryAdresse(orders.getDilevryAdresse());
                newOrder.setSecondCartItemList(cartItemList1);
                orderRepo.save(newOrder);
                try {
                    invoiceService.AddPDFInvoice(newOrder.getId());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
             else if (claims.getTypeClaim() == TypeClaim.DELIVERY) {
                Orders orders = claims.getOrder();
                adminService.suspendUser(orders.getDeliveryS().getLivreur().getEmail());
                emailService.sendClaimEmail(orders.getDeliveryS().getLivreur().getEmail(), "there is a claim about your service: " + claims.getDescription()+"So you are banned for a week ");
            if (orders.getDeliveryS().getLivreur().getBanNumber()>=7){
                adminService.banUser(orders.getDeliveryS().getLivreur().getEmail());
                emailService.sendClaimEmail(orders.getDeliveryS().getLivreur().getEmail(), "You are claimed 7 times So you are banned ");
            }

            }
            emailService.sendClaimEmail(email, "your claim was resolved we will see what shoul as do about " + claims.getDescription());
        } else {
            emailService.sendClaimEmail(email, "your claim was rejected");
        }
        claimsRepo.save(claims);
    }*/
}

