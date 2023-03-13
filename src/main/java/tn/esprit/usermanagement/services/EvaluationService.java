package tn.esprit.usermanagement.services;

import tn.esprit.usermanagement.entities.Evaluation;

import java.util.List;

public interface EvaluationService {
    public List<Evaluation> getEvaluationsByDeliveryId(Integer deliveryId);
}
