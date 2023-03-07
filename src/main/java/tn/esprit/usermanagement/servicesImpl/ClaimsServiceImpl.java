package tn.esprit.usermanagement.servicesImpl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Claims;
import tn.esprit.usermanagement.entities.Pictures;
import tn.esprit.usermanagement.entities.Product;
import tn.esprit.usermanagement.entities.User;
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
    ProductRepo productRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    PicturesRepo picturesRepo;

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
    public List<Claims> ShowClaimsByProduct(int ProductId) {
Product product = productRepo.findById(ProductId).get();
return product.getClaims();
    }


    @Override
    public Claims AddClaimsWithPicturesAndAssignToUser(Integer userId, Claims claim, List<MultipartFile> files) throws IOException {
        User user = userRepo.findById(userId).get();
        claim.setCreatedAt(LocalDateTime.now());
        claim.setConsultAt(null);
        claim.setStatusClaims(StatusClaims.Pending);
        claim.setUser(user);
        Claims savedClaim = claimsRepo.save(claim);
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
        picturesRepo.saveAll(picturesList);
        savedClaim.setPictures(picturesList);
        return claimsRepo.save(savedClaim);    }

    @Override
    public Claims AddClaimsToProductsWithPicturesAndAssignToUser(List<Integer> productIds, Integer userId, Claims claim, List<MultipartFile> files) throws IOException {
        User user = userRepo.findById(userId).get();
        claim.setUser(user);
        claim.setCreatedAt(LocalDateTime.now());
        claim.setConsultAt(null);
        claim.setStatusClaims(StatusClaims.Pending);
        List<Product> productsList = productRepo.findAllById(productIds);
        claim.setProducts(productsList);
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
    public void UpdateClaim(Integer ClaimId,StatusClaims status) {
        Claims claims=claimsRepo.findById(ClaimId).get();
        claims.setStatusClaims(status);
        claims.setConsultAt(LocalDateTime.now());
        claimsRepo.save(claims);

    }
    @Scheduled(fixedRate = 10000) // runs every second
    @Override
    public void DeleteRejectedClaims() {
        LocalDateTime limite = LocalDateTime.now().minusMonths(12);
        List<Claims> RejetedClaims = claimsRepo.findByStatusClaimsAndConsultAtIsBefore (StatusClaims.Rejected, limite);
        for (Claims claims : RejetedClaims) {
            claimsRepo.delete(claims);
        }
    }
    @Transactional
    @Override
    public String UpdateClaims(List<Integer> productIds, Claims claims, List<MultipartFile> files) throws IOException {
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
                if (productIds == null) {
                    claims.setProducts(existingClaims.getProducts());
                } else {
                    claims.setProducts(productRepo.findAllById(productIds));
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
