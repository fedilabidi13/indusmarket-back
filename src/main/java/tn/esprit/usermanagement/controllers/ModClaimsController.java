package tn.esprit.usermanagement.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.Claims;
import tn.esprit.usermanagement.enumerations.StatusClaims;
import tn.esprit.usermanagement.enumerations.TypeClaim;
import tn.esprit.usermanagement.servicesImpl.ClaimsServiceImpl;
import java.util.List;

@RestController
@RequestMapping("/mod/claims")
public class ModClaimsController {
    @Autowired
    ClaimsServiceImpl claimsService;
    //http://localhost:8085/mod/claims/orderClaimTreatment/{claimId}/{status}
    @PutMapping("/orderClaimTreatment/{claimId}/{status}")
    public String OrderClaimTreatment(@PathVariable("claimId") Integer ClaimId, @PathVariable("status") StatusClaims status) {
      return claimsService.OrderClaimTreatment(ClaimId, status);
    }

    //http://localhost:8085/mod/claims/postClaimTreatment/{claimId}/{status}
    @PutMapping("/postClaimTreatment/{claimId}/{status}")
    public String PostClaimTreatment(@PathVariable("claimId") Integer ClaimId, @PathVariable("status") StatusClaims status) {
       return claimsService.PostClaimTreatment(ClaimId, status);
    }

    //http://localhost:8085/mod/claims/deliviryClaimTreatment/{claimId}/{status}
    @PutMapping("/deliviryClaimTreatment/{claimId}/{status}")
    public String DeliviryClaimTreatment(@PathVariable("claimId") Integer ClaimId, @PathVariable("status") StatusClaims status) {
       return claimsService.DeliviryClaimTreatment(ClaimId, status);
    }

    //http://localhost:8085/mod/claims/otherClaimTreatment/{claimId}/{status}
    @PutMapping("/otherClaimTreatment/{claimId}/{status}")
    public void OtherClaimTreatment(@PathVariable("claimId") Integer ClaimId, @PathVariable("status") StatusClaims status) {
        claimsService.OtherClaimTreatment(ClaimId, status);
    }

    //http://localhost:8085/mod/claims/allClaims
    @GetMapping("/allClaims")
    public List<Claims> ShowAllClaims() {
        return claimsService.ShowAllClaims();
    }

    //http://localhost:8085/mod/claims/claimsForOrder/{orderId}
    @GetMapping("/claimsForOrder/{orderId}")
    public List<Claims> ShowClaimsByOrder(@PathVariable("orderId") int orderId) {
        return claimsService.ShowClaimsByOrder(orderId);
    }
    //http://localhost:8085/mod/claims/allClaimsByType/{type}
    @GetMapping("/allClaimsByType/{type}")
    public List<Claims> ShowClaimsByType(@PathVariable("type") TypeClaim typeClaim) {
        return claimsService.ShowClaimsByType(typeClaim);
    }
}

