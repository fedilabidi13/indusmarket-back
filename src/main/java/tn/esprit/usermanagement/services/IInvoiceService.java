package tn.esprit.usermanagement.services;

import tn.esprit.usermanagement.entities.Invoice;

public interface IInvoiceService {
    public Invoice createInvoice (Integer idOrder);
}
