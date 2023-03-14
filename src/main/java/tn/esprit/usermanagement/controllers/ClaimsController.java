package tn.esprit.usermanagement.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    //http://localhost:8085/claims/addClaims
    @PostMapping("/addClaims")
    public Claims addClaimsWithPictures( @ModelAttribute Claims claim, @RequestParam(value = "file",required = false) List<MultipartFile> files) throws IOException {
        return claimsService.AddClaimsWithPicturesAndAssignToUser(claim, files);
    }

    //http://localhost:8085/claims/claimsForUser
    @GetMapping("/claimsForUser")
    public List<Claims> ShowClaimsByUser() {
        return claimsService.ShowClaimsByUser();
    }

    //http://localhost:8085/claims/addOrdersClaims/{orderId}
    @PostMapping("/addOrdersClaims/{orderId}")
    public String AddOrdersClaimsToOrderWithPicturesAndAssignToUser(@PathVariable("orderId") Integer orderId, @ModelAttribute Claims claim, @RequestParam("file") List<MultipartFile> files) throws IOException {
        return claimsService.AddOrdersClaimsToOrderWithPicturesAndAssignToUser(orderId, claim, files);
    }

    //http://localhost:8085/claims/addPostClaims/{postId}
    @PostMapping("/addPostClaims/{postId}")
    public String AddClaimsToPostWithPicturesAndAssignToUser(@PathVariable("postId") Integer postId, @ModelAttribute Claims claim, @RequestParam("file") List<MultipartFile> files) throws IOException{
        return claimsService.AddClaimsToPostWithPicturesAndAssignToUser(postId,claim,files);
    }

    //http://localhost:8085/claims/addDeliveryClaims/{orderId}
    @PostMapping("/addDeliveryClaims/{orderId}")
    public String AddDeliveryClaimsToOrderWithPicturesAndAssignToUser(@PathVariable("orderId") Integer orderId,@ModelAttribute Claims claim, @RequestParam("file") List<MultipartFile> files) throws IOException {
        return claimsService.AddDeliveryClaimsToOrderWithPicturesAndAssignToUser(orderId, claim, files);
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
    //http://localhost:8085/claims/orderClaimTreatment/{claimId}/{status}
    @PutMapping("/orderClaimTreatment/{claimId}/{status}")
    public String OrderClaimTreatment(@PathVariable("claimId") Integer ClaimId, @PathVariable("status") StatusClaims status) {
        return claimsService.OrderClaimTreatment(ClaimId, status);
    }
    //http://localhost:8085/claims/postClaimTreatment/{claimId}/{status}
    @PutMapping("/postClaimTreatment/{claimId}/{status}")
    public String PostClaimTreatment(@PathVariable("claimId") Integer ClaimId, @PathVariable("status") StatusClaims status) {
        return claimsService.PostClaimTreatment(ClaimId, status);
    }

    //http://localhost:8085/claims/deliviryClaimTreatment/{claimId}/{status}
    @PutMapping("/deliviryClaimTreatment/{claimId}/{status}")
    public String DeliviryClaimTreatment(@PathVariable("claimId") Integer ClaimId, @PathVariable("status") StatusClaims status) {
        return claimsService.DeliviryClaimTreatment(ClaimId, status);
    }

    //http://localhost:8085/claims/mod/otherClaimTreatment/{claimId}/{status}
    @PutMapping("/otherClaimTreatment/{claimId}/{status}")
    public void OtherClaimTreatment(@PathVariable("claimId") Integer ClaimId, @PathVariable("status") StatusClaims status) {
        claimsService.OtherClaimTreatment(ClaimId, status);
    }

    //http://localhost:8085/claims/allClaims
    @GetMapping("/allClaims")
    public List<Claims> ShowAllClaims() {
        return claimsService.ShowAllClaims();
    }

    //http://localhost:8085/claims/mod/claimsForOrder/{orderId}
    @GetMapping("/claimsForOrder/{orderId}")
    public List<Claims> ShowClaimsByOrder(@PathVariable("orderId") int orderId) {
        return claimsService.ShowClaimsByOrder(orderId);
    }
    //http://localhost:8085/claims/mod/allClaimsByType/{type}
    @GetMapping("/allClaimsByType/{type}")
    public List<Claims> ShowClaimsByType(@PathVariable("type") TypeClaim typeClaim) {
        return claimsService.ShowClaimsByType(typeClaim);
    }
}

