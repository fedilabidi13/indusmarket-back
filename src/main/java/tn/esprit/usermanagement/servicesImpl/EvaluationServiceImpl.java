package tn.esprit.usermanagement.servicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.entities.Evaluation;
import tn.esprit.usermanagement.repositories.EvaluationRepo;
import tn.esprit.usermanagement.services.EvaluationService;

import java.util.List;

@Service
public class EvaluationServiceImpl implements EvaluationService {
    @Autowired
    private EvaluationRepo evaluationRepository;


    public Evaluation addEvaluation(Evaluation evaluation) {
        return evaluationRepository.save(evaluation);
    }

    @Override
    public List<Evaluation> getEvaluationsByDeliveryId(Integer  deliveryId) {
        return evaluationRepository.findByDeliveryId(deliveryId);    }
}
