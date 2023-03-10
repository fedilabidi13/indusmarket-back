package tn.esprit.usermanagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.usermanagement.entities.Invoice;
import tn.esprit.usermanagement.services.IInvoiceService;

import java.io.IOException;

@RestController
public class InvoiceController {
    @Autowired
    private IInvoiceService invoiceService;


    @PostMapping("/PDFinvoice/{orderId}")
    void AddPDFInvoice ( @PathVariable("orderId") Integer orderId) throws IOException {

        invoiceService.AddPDFInvoice(orderId);
    }


    @PostMapping("/CreateInvoice/{idOrder}")

    public Invoice createInvoice (@PathVariable("idOrder") Integer idOrder)
    {
        return invoiceService.createInvoice(idOrder);
    }

}
