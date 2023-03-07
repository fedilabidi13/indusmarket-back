package tn.esprit.usermanagement.servicesImpl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.entities.Invoice;
import tn.esprit.usermanagement.repositories.InvoiceRepo;
import tn.esprit.usermanagement.repositories.OrderRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.IInvoiceService;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor

public class InvoiceImpl  implements IInvoiceService {

    private OrderRepo orderRepo;
    private final UserRepo userRepo;
    private RefGenerator refGenerator;

    @Override
    public Invoice createInvoice (Integer idOrder) {
     Invoice theInvoice = new Invoice();
     theInvoice.setInvoiceRef(refGenerator.generateRef());
     theInvoice.setCreationDate(LocalDateTime.now());
     theInvoice.setOrdre(orderRepo.getReferenceById(idOrder));

     return theInvoice;
    }
}
