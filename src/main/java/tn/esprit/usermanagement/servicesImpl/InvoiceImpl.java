package tn.esprit.usermanagement.servicesImpl;

import com.google.zxing.WriterException;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.stripe.model.Order;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.usermanagement.entities.*;
import tn.esprit.usermanagement.repositories.InvoiceRepo;
import tn.esprit.usermanagement.repositories.OrderRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.IInvoiceService;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class InvoiceImpl  implements IInvoiceService {

    private OrderRepo orderRepo;
    private final UserRepo userRepo;
    private RefGenerator refGenerator;
    private final InvoiceRepo invoiceRepo;

    @Override
    public Invoice createInvoice (Integer idOrder) {
     Invoice theInvoice = new Invoice();
     theInvoice.setInvoiceRef(refGenerator.generateRef());
     theInvoice.setCreationDate(LocalDateTime.now());
     theInvoice.setOrdre(orderRepo.getReferenceById(idOrder));
     invoiceRepo.save(theInvoice);
     return theInvoice;
    }


    @Override
    public void AddPDFInvoice ( Integer orderId) throws IOException {


        Invoice theInvoice = new Invoice();
        theInvoice.setInvoiceRef(refGenerator.generateRef());
        theInvoice.setCreationDate(LocalDateTime.now());
        theInvoice.setOrdre(orderRepo.getReferenceById(orderId));
        User user = userRepo.findById(orderRepo.getReferenceById(orderId).getUser().getId()).orElse(null);


        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(pdfOutputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);


        Image logo = new Image(ImageDataFactory.create("src/main/resources/assets/1.png"));

        logo.setWidth(100);
        logo.setHeight(50);

        logo.setFixedPosition(10, 800);

        document.add(logo);

        // Add a title to the PDF
        Paragraph title = new Paragraph("A purchase order   : " + theInvoice.getInvoiceRef());
        title.setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        // Add a description to the PDF
        Paragraph description = new Paragraph("The date that the order was placed was " + theInvoice.getCreationDate() +".");
        description.setTextAlignment(TextAlignment.JUSTIFIED);
        document.add(description);




        // Add order items to the PDF
        Table table = new Table(new float[]{4, 1, 2});
        table.setWidth(UnitValue.createPercentValue(100));
        table.addCell(new Cell().add(new Paragraph("Product")));
        table.addCell(new Cell().add(new Paragraph("Quantity")));
        table.addCell(new Cell().add(new Paragraph("Price")));




        //Integer idUser = authenticationService.currentlyAuthenticatedUser().getId();
        List<CartItem> orderItems =theInvoice.getOrdre().getSecondCartItemList();





        for (CartItem item : orderItems) {
            table.addCell(new Cell().add(new Paragraph(item.getProduct().getName())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getQuantity()))));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getProduct().getPrice()))));
        }
        document.add(table);


        // Add other information to the PDF
        Paragraph userInfo = new Paragraph("User Information:");
        userInfo.setBold();
        userInfo.setMarginTop(20);
        document.add(userInfo);


        Paragraph name = new Paragraph("Name: " + user.getFirstName() + " " + user.getLastName());
        document.add(name);


        Paragraph email = new Paragraph("Delivery adresse: " + orderRepo.getReferenceById(orderId).getDilevryAdresse());
        document.add(email);


        Paragraph Total = new Paragraph("Total: " + orderRepo.getReferenceById(orderId).getTotalAmount());
        log.error("le total est :"+ String.valueOf(orderRepo.getReferenceById(orderId).getTotalAmount()));
        document.add(Total);


        // Close the PDF document
        document.close();
        byte[] pdfData = pdfOutputStream.toByteArray();


        // Save the PDF to a file
        String fileName = theInvoice.getInvoiceRef() + ".pdf";
        String filePath = "src/main/resources/assets/" + fileName;
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(pdfData);
        fos.flush();
        fos.close();
        invoiceRepo.save(theInvoice);


    }
}
