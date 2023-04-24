package tn.esprit.usermanagement.services;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Claims;
import tn.esprit.usermanagement.enumerations.StatusClaims;
import tn.esprit.usermanagement.enumerations.TypeClaim;
import java.io.IOException;
import java.util.List;
public interface ClaimsService {
    public List<Claims> ShowAllClaims();
    public List<Claims> ShowClaimsByType(TypeClaim typeClaim);
    public List<Claims> ShowClaimsByUser();
    public List<Claims> ShowClaimsByOrder(int orderId);
    public Claims AddClaimsWithPicturesAndAssignToUser( Claims claim,List<MultipartFile> files) throws IOException;
    public String AddOrdersClaimsToOrderWithPicturesAndAssignToUser(Integer orderId, Claims claim, List<MultipartFile> files) throws IOException;
    public String AddDeliveryClaimsToOrderWithPicturesAndAssignToUser(Integer orderId, Claims claim, List<MultipartFile> files) throws IOException;
    public String AddClaimsToPostWithPicturesAndAssignToUser(Integer postId, Claims claim, List<MultipartFile> files) throws IOException;
    public void UpdatePendingClaims();
    public void DeleteRejectedClaims();
    public String updateDeliveryClaims(Integer orderId, Claims newClaims, List<MultipartFile> files) throws IOException;
    public String updateOrderClaims(Integer orderId, Claims newClaims, List<MultipartFile> files) throws IOException;
    public String OrderClaimTreatment(Integer claimId, StatusClaims status);
    public String PostClaimTreatment(Integer claimId, StatusClaims status);
    public String DeliviryClaimTreatment(Integer claimId, StatusClaims status);
    public String OtherClaimTreatment(Integer claimId, StatusClaims status);
    public void DeleteClaim(Integer id);

}
