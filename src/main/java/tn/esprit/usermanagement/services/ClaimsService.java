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
    public List<Claims> ShowClaimsByOrder(int orderId);
    public Claims AddClaimsWithPicturesAndAssignToUser(Integer userId, Claims claim,List<MultipartFile> files) throws IOException;
   public String AddClaimsToOrderWithPicturesAndAssignToUser(Integer orderId ,Integer userId, Claims claim, List<MultipartFile> files,TypeClaim typeClaim) throws IOException;
    public Claims AddClaimsToPostWithPicturesAndAssignToUser(Integer postId, Integer userId, Claims claim, List<MultipartFile> files) throws IOException;
   public void UpdatePendingClaims();
    public void Claimtreatment(Integer ClaimId,StatusClaims status);
    public void DeleteRejectedClaims();
    public String UpdateClaims(Integer orderId,Claims claims, List<MultipartFile> files) throws IOException;




}
