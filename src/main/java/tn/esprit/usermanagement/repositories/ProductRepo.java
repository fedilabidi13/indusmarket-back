package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.usermanagement.dto.ProductSpecification;
import tn.esprit.usermanagement.dto.SearchCriteria;
import tn.esprit.usermanagement.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Integer>, JpaSpecificationExecutor<Product> {

    @Query("select p from Product p where p.discount is null")
    public List<Product> ShowAllProductsWithoutDiscount();
    @Query("select p from Product p where  p.discount is not null ")
    public List<Product> ShowAllProductsWithDiscount();

    @Query("select p from Product p where p.category =?1")
    public List<Product> SortProductByCategory(String category);
    Product findByReference(String ref);
    List<Product> findByPriceBetween(float minPrice, float maxPrice);




    default List<Product> searchProducts(String reference, String name, Integer quantity,
                                         Float price, String description, String brand) {
        List<SearchCriteria> searchCriteriaList = new ArrayList<>();

        if (reference != null) {
            searchCriteriaList.add(new SearchCriteria("reference", ":", reference));
        }
        if (name != null) {
            searchCriteriaList.add(new SearchCriteria("name", ":", name));
        }
        if (quantity != null) {
            searchCriteriaList.add(new SearchCriteria("quantity", ":", quantity));
        }
        if (price != null) {
            searchCriteriaList.add(new SearchCriteria("price", ":", price));
        }
        if (description != null) {
            searchCriteriaList.add(new SearchCriteria("description", ":", description));
        }
        if (brand != null) {
            searchCriteriaList.add(new SearchCriteria("brand", ":", brand));
        }

        Specification<Product> spec = Specification.where(null);
        for (SearchCriteria criteria : searchCriteriaList) {
            spec = spec.and(new ProductSpecification(criteria));
        }

        return findAll(spec);
    }
}
