package tn.esprit.usermanagement.services;
import tn.esprit.usermanagement.entities.Invoice;
import java.io.IOException;

public interface IInvoiceService {
     Invoice createInvoice (Integer idOrder);

     void AddPDFInvoice ( Integer orderId) throws IOException ;
}
