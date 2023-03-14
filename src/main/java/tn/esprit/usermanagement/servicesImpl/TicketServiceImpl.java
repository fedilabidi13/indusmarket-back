package tn.esprit.usermanagement.servicesImpl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.itextpdf.layout.element.Image;
import tn.esprit.usermanagement.entities.Event;
import tn.esprit.usermanagement.entities.ForumEntities.Pictures;
import tn.esprit.usermanagement.entities.Ticket;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.EventRepo;
import tn.esprit.usermanagement.repositories.PicturesRepo;
import tn.esprit.usermanagement.repositories.TicketRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.TicketService;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    TicketRepo ticketRepo;
    @Autowired
    EventRepo eventRepo;
    @Autowired
    PicturesRepo picturesRepo;
    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    RefGenerator refGenerator;
    @Override
    public Ticket AddTicketForEventAndAssignToUser(Ticket ticket,Integer eventId) throws IOException, WriterException {
        User user = userRepo.findById(authenticationService.currentlyAuthenticatedUser().getId()).get();
        if (ticketRepo.findByUserAndEvent(user,eventRepo.findById(eventId).get())!=null){
            return ticketRepo.findByUserAndEvent(user,eventRepo.findById(eventId).get());
        }
        Ticket savedTicket = ticketRepo.save(ticket);
        savedTicket.setReference(refGenerator.generateTicketRef());
        savedTicket.setUser(user);
        savedTicket.setEvent(eventRepo.findById(eventId).get());
        // Generate the QR code data
        String qrCodeText = savedTicket.getId().toString() + " "
                + savedTicket.getDescreption() + " "
                + "Le nom est : " + savedTicket.getUser().getFirstName() + " Le prenom est : "
                + savedTicket.getUser().getLastName();
        // Create a new PNG image
        int width = 500;
        int height = 250;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();

        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(pdfOutputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);
        // Add a title to the PDF
        document.add(new Paragraph("\n")); // add a blank line
        Paragraph title = new Paragraph(savedTicket.getReference());
        title.setTextAlignment(TextAlignment.CENTER);
        document.add(title);
        document.add(new Paragraph("\n")); // add a blank line
        document.add(new Paragraph("\n")); // add a blank line
        // Add a description to the PDF
        Paragraph description = new Paragraph("This ticket is for " + eventRepo.findById(eventId).get().getTitle());
        description.setTextAlignment(TextAlignment.JUSTIFIED);
        document.add(description);
        document.add(new Paragraph("\n")); // add a blank line
        document.add(new Paragraph("\n")); // add a blank line
        // Add a description to the PDF
        Paragraph description1 = new Paragraph( "which is taking place on " + eventRepo.findById(eventId).get().getAdresse() + " at " + eventRepo.findById(eventId).get().getStartDate() + ".");
        description.setTextAlignment(TextAlignment.JUSTIFIED);
        document.add(description1);
        document.add(new Paragraph("\n")); // add a blank line
        document.add(new Paragraph("\n")); // add a blank line
        // Add the QR code image to the PDF
        Image qrCodePdfImage = new Image(ImageDataFactory.create(pngData));
        qrCodePdfImage.scaleToFit(500, 500);
        qrCodePdfImage.setHorizontalAlignment(HorizontalAlignment.CENTER);
        document.add(qrCodePdfImage);
        // Add other information to the PDF
        Paragraph userInfo = new Paragraph("Client:");
        userInfo.setBold();
        userInfo.setMarginTop(20);
        document.add(userInfo);
        Paragraph name = new Paragraph(user.getFirstName() + " " + user.getLastName());
        document.add(name);
        Paragraph email = new Paragraph(user.getEmail());
        document.add(email);
        document.add(new Paragraph("\n")); // add a blank line
        document.add(new Paragraph("\n")); // add a blank line
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy 'at' HH:mm");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        // Create the paragraph with the formatted date and time
        Paragraph date = new Paragraph(formattedDateTime);
        date.setTextAlignment(TextAlignment.CENTER);
        document.add(date);
        Image logo = new Image(ImageDataFactory.create("src/main/resources/assets/logo.png"));
        logo.setWidth(75);
        logo.setHeight(50);
        logo.setFixedPosition(document.getLeftMargin()-20 , document.getPageEffectiveArea(document.getPdfDocument().getDefaultPageSize()).getTop() - 20);
        document.add(logo);
        // Close the PDF document
        document.close();
        byte[] pdfData = pdfOutputStream.toByteArray();
        // Save the PDF to a file
        String fileName = savedTicket.getReference() + ".pdf";
        String filePath = "src/main/resources/assets/" + fileName;
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(pdfData);
        fos.flush();
        fos.close();
        // Create a new Pictures object with the PDF data
        Pictures pdfPicture = new Pictures();
        pdfPicture.setData(pdfData);
        pdfPicture.setContentType("application/pdf");
        picturesRepo.save(pdfPicture);
        // Add the Pictures object to the list of QR codes in the Ticket object
        List<Pictures> PdfImage = new ArrayList<>();
        PdfImage.add(pdfPicture);
        savedTicket.setFiles(PdfImage);
        return ticketRepo.save(savedTicket);
    }
}
