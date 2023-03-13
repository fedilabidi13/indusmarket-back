package tn.esprit.usermanagement.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.usermanagement.entities.Delivery;
import tn.esprit.usermanagement.entities.Evaluation;
import tn.esprit.usermanagement.repositories.DeliveryRepo;
import tn.esprit.usermanagement.repositories.EvaluationRepo;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/Evaluation")
public class EvaluationController {

    private DeliveryRepo deliveryRepository;
    private EvaluationRepo evaluationRepository;



    @PostMapping("/{idDelivery}")
    public Evaluation addEvaluation(@PathVariable Integer idDelivery ,@RequestBody Evaluation evaluation) {
        Delivery delivery =deliveryRepository.getReferenceById(idDelivery);
        evaluation.setDeliveryId(delivery);
        evaluation.setDateHeureEvaluation(LocalDateTime.now());
        return  evaluationRepository.save(evaluation);
    }

}
