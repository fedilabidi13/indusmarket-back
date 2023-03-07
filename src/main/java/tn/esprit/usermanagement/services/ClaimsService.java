package tn.esprit.usermanagement.services;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Claims;
import tn.esprit.usermanagement.enumerations.StatusClaims;
import tn.esprit.usermanagement.enumerations.TypeClaim;

import java.io.IOException;
import java.util.List;

public interface ClaimsService {
    public Claims addClaims(Claims claims);
    public List<Claims> ShowAllClaims();
    public List<Claims> ShowClaimsByType(TypeClaim typeClaim);
    public List<Claims> ShowClaimsByUser(int UserId);
    public List<Claims> ShowClaimsByProduct(int ProductId);
    public Claims AddClaimsWithPicturesAndAssignToUser(Integer userId, Claims claim,List<MultipartFile> files) throws IOException;
   public Claims AddClaimsToProductsWithPicturesAndAssignToUser(List<Integer> productIds ,Integer userId, Claims claim, List<MultipartFile> files) throws IOException;
   public void UpdatePendingClaims();
   public void UpdateClaim(Integer ClaimId, StatusClaims status);
    public void DeleteRejectedClaims();
    public String UpdateClaims(List<Integer> productIds,Claims claims, List<MultipartFile> files) throws IOException;



}
