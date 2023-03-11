package tn.esprit.usermanagement.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Claims;
import tn.esprit.usermanagement.enumerations.StatusClaims;
import tn.esprit.usermanagement.enumerations.TypeClaim;
import tn.esprit.usermanagement.servicesImpl.ClaimsServiceImpl;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/claims")
public class ClaimsController {
    @Autowired
    ClaimsServiceImpl claimsService;
    //http://localhost:8085/claims/AllClaims
    @GetMapping("/AllClaims")
    public List<Claims> ShowAllClaims() {
        return claimsService.ShowAllClaims();
    }
    //http://localhost:8085/claims/AddClaimsWithPicture

    @PostMapping("/AddClaimsWithPicture")
    public Claims addClaimsWithPictures( @ModelAttribute Claims claim, @RequestParam("file") List<MultipartFile> files) throws IOException {
        return claimsService.AddClaimsWithPicturesAndAssignToUser(claim, files);
    }
    //http://localhost:8085/claims/ShowClaimsByUser

    @GetMapping("/ShowClaimsByUser")
    public List<Claims> ShowClaimsByUser() {
        return claimsService.ShowClaimsByUser();
    }
    //http://localhost:8085/claims/AllClaimsByType/{type}

    @GetMapping("/AllClaimsByType/{type}")
    public List<Claims> ShowClaimsByType(@PathVariable("type") TypeClaim typeClaim) {
        return claimsService.ShowClaimsByType(typeClaim);
    }
    //http://localhost:8085/claims/AddOrdersClaimsToOrderWithPicturesAndAssignToUser/{orderId}

    @PostMapping("/AddOrdersClaimsToOrderWithPicturesAndAssignToUser/{orderId}")
    public String AddOrdersClaimsToOrderWithPicturesAndAssignToUser(@PathVariable("orderId") Integer orderId, @ModelAttribute Claims claim, @RequestParam("file") List<MultipartFile> files) throws IOException {
        return claimsService.AddOrdersClaimsToOrderWithPicturesAndAssignToUser(orderId, claim, files);
    }
    //http://localhost:8085/claims/AddClaimsToPostWithPicturesAndAssignToUser/{postId}

    @PostMapping("/AddClaimsToPostWithPicturesAndAssignToUser/{postId}")
    public String AddClaimsToPostWithPicturesAndAssignToUser(@PathVariable("postId") Integer postId, @ModelAttribute Claims claim, @RequestParam("file") List<MultipartFile> files) throws IOException{
        return claimsService.AddClaimsToPostWithPicturesAndAssignToUser(postId,claim,files);
    }
    //http://localhost:8085/claims/AddDeliveryClaimsToOrderWithPicturesAndAssignToUser/{orderId}

    @PostMapping("/AddDeliveryClaimsToOrderWithPicturesAndAssignToUser/{orderId}")
    public String AddDeliveryClaimsToOrderWithPicturesAndAssignToUser(@PathVariable("orderId") Integer orderId,@ModelAttribute Claims claim, @RequestParam("file") List<MultipartFile> files) throws IOException {
        return claimsService.AddDeliveryClaimsToOrderWithPicturesAndAssignToUser(orderId, claim, files);
    }
    //http://localhost:8085/claims/ClaimsForOrder/{orderId}

    @GetMapping("/ClaimsForOrder/{orderId}")
    public List<Claims> ShowClaimsByOrder(@PathVariable("orderId") int orderId) {
        return claimsService.ShowClaimsByOrder(orderId);
    }
    //http://localhost:8085/claims/OrderClaimTreatment/{ClaimId}/{status}


    @PutMapping("/OrderClaimTreatment/{ClaimId}/{status}")
    public void OrderClaimTreatment(@PathVariable("ClaimId") Integer ClaimId, @PathVariable("status") StatusClaims status) {
        claimsService.OrderClaimTreatment(ClaimId, status);
    }
    //http://localhost:8085/claims/PostClaimTreatment/{ClaimId}/{status}

    @PutMapping("/PostClaimTreatment/{ClaimId}/{status}")
    public void PostClaimTreatment(@PathVariable("ClaimId") Integer ClaimId, @PathVariable("status") StatusClaims status) {
        claimsService.PostClaimTreatment(ClaimId, status);
    }
    //http://localhost:8085/claims/DeliviryClaimTreatment/{ClaimId}/{status}

    @PutMapping("/DeliviryClaimTreatment/{ClaimId}/{status}")
    public void DeliviryClaimTreatment(@PathVariable("ClaimId") Integer ClaimId, @PathVariable("status") StatusClaims status) {
        claimsService.DeliviryClaimTreatment(ClaimId, status);
    }
    //http://localhost:8085/claims/OtherClaimTreatment/{ClaimId}/{status}

    @PutMapping("/OtherClaimTreatment/{ClaimId}/{status}")
    public void OtherClaimTreatment(@PathVariable("ClaimId") Integer ClaimId, @PathVariable("status") StatusClaims status) {
        claimsService.OtherClaimTreatment(ClaimId, status);
    }
    //http://localhost:8085/claims/updateDeliveryClaims

    @PutMapping("/updateDeliveryClaims")
    public String updateDeliveryClaims(@RequestParam(value = "orderId",required = false) Integer orderId,@ModelAttribute Claims claims, @RequestParam(value = "files",required = false) List<MultipartFile> files) throws IOException {
        return claimsService.updateDeliveryClaims(orderId,claims, files);
    }
    //http://localhost:8085/claims/updateOrderClaims

    @PutMapping("/updateOrderClaims")
    public String updateOrderClaims(@RequestParam(value = "orderId") Integer orderId,@ModelAttribute Claims claims, @RequestParam(value = "files",required = false) List<MultipartFile> files) throws IOException {
        return claimsService.updateOrderClaims(orderId,claims, files);
    }
}

