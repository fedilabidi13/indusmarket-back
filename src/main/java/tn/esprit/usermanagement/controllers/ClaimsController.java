package tn.esprit.usermanagement.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.usermanagement.entities.Claims;
import tn.esprit.usermanagement.servicesImpl.ClaimsServiceImpl;
import tn.esprit.usermanagement.servicesImpl.FileUploadImpl;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/claims")
public class ClaimsController {
    @Autowired
    ClaimsServiceImpl claimsService;
    @Autowired
    FileUploadImpl fileUpload;

    //http://localhost:8085/claims/addClaims
    @PostMapping("/addClaims")
    public Claims addClaimsWithPictures( @ModelAttribute Claims claim, @RequestParam("file") List<MultipartFile> files) throws IOException {
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
    @PostMapping("/test")
    public List<String> test(@RequestParam("files") List<MultipartFile> files) throws IOException{
        return fileUpload.uploadFiles(files);
    }
}

