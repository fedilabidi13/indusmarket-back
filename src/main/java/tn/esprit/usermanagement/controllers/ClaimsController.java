package tn.esprit.usermanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import tn.esprit.usermanagement.entities.Claims;
import tn.esprit.usermanagement.entities.Pictures;
import tn.esprit.usermanagement.enumerations.StatusClaims;
import tn.esprit.usermanagement.enumerations.TypeClaim;
import tn.esprit.usermanagement.repositories.ClaimsRepo;
import tn.esprit.usermanagement.repositories.PicturesRepo;
import tn.esprit.usermanagement.servicesImpl.ClaimsServiceImpl;
import tn.esprit.usermanagement.servicesImpl.PicturesServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RestController
public class ClaimsController {
    @Autowired
    ClaimsRepo claimsRepo;

    @Autowired
    ClaimsServiceImpl claimsService;

    @Autowired
    PicturesRepo picturesRepo;

    @GetMapping("/AllClaims")
    public List<Claims> ShowAllClaims() {
        return claimsService.ShowAllClaims();
    }

    @PostMapping("/AddClaimsWithPicture/{userId}")
    public Claims addClaimsWithPictures(@PathVariable("userId") int userId, @ModelAttribute Claims claim, @RequestParam("file") List<MultipartFile> files) throws IOException {
        return claimsService.AddClaimsWithPicturesAndAssignToUser(userId, claim, files);
    }

    @GetMapping("/AllClaims/{userId}")
    public List<Claims> ShowClaimsByUser(@PathVariable("userId") int UserId) {
        return claimsService.ShowClaimsByUser(UserId);
    }

    @GetMapping("/AllClaimsByType/{type}")
    public List<Claims> ShowClaimsByType(@PathVariable("type") TypeClaim typeClaim) {
        return claimsService.ShowClaimsByType(typeClaim);
    }

    @PostMapping("/addClaimWithProductAndPictureAndAssignToUser/{userId}")
    public Claims AddClaimsToProductsWithPicturesAndAssignToUser(@RequestParam("productIds") List<Integer> productIds, @PathVariable("userId") Integer userId, Claims claim, @RequestParam("file") List<MultipartFile> files) throws IOException {
        return claimsService.AddClaimsToProductsWithPicturesAndAssignToUser(productIds, userId, claim, files);
    }

    @GetMapping("/ClaimsForProduct/{ProductId}")
    public List<Claims> ShowClaimsByProduct(@PathVariable("ProductId") int ProductId) {
        return claimsService.ShowClaimsByProduct(ProductId);
    }

    @PutMapping("/UpdateClaim/{ClaimId}/{status}")
    public void UpdateClaim(@PathVariable("ClaimId") Integer ClaimId, @PathVariable("status") StatusClaims status) {
        claimsService.UpdateClaim(ClaimId, status);
    }
@PutMapping("/updateClaim")
public String UpdateClaims(@RequestParam(value = "productIds",required = false) List<Integer> productIds,@ModelAttribute Claims claims, @RequestParam(value = "files",required = false) List<MultipartFile> files) throws IOException {
        return claimsService.UpdateClaims(productIds,claims, files);
}

    @GetMapping("/pictures/{id}")
    public ResponseEntity<List<ByteArrayResource>> getPictures(@PathVariable Integer id) {
        Claims claims = claimsRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Claims not found"));

        List<ByteArrayResource> resources = new ArrayList<>();
        for (Pictures picture : claims.getPictures()) {
            ByteArrayResource resource = new ByteArrayResource(picture.getData());
            resources.add(resource);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_MIXED);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resources);
    }
    /*@GetMapping("/load")
    public String affichage() throws IOException {
        byte[] imageData = picturesRepo.getById(123).getData();
        String encodedString = Base64
                .getEncoder()
                .encodeToString(imageData);
        String res = "<img src=\"data:image/png;base64, ";
        res+=encodedString;
        res+="\" alt=\"Red dot\" />";
        return res;

    }*/
}

