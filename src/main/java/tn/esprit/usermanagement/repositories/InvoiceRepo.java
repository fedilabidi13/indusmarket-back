package tn.esprit.usermanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.usermanagement.entities.Invoice;

public interface InvoiceRepo extends JpaRepository<Invoice,Integer>{
}
