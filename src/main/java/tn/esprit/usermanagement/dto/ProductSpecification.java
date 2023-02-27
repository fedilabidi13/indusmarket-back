package tn.esprit.usermanagement.dto;



import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import tn.esprit.usermanagement.entities.Product;


public class ProductSpecification implements Specification<Product> {

    private SearchCriteria criteria;

    public ProductSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }


    public jakarta.persistence.criteria.Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return (jakarta.persistence.criteria.Predicate) builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return (jakarta.persistence.criteria.Predicate) builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return (jakarta.persistence.criteria.Predicate) builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return (jakarta.persistence.criteria.Predicate) builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }
}
