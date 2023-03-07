package tn.esprit.usermanagement.servicesImpl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code93Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.element.Image;
import tn.esprit.usermanagement.entities.Event;
import tn.esprit.usermanagement.entities.Pictures;
import tn.esprit.usermanagement.entities.Ticket;
import tn.esprit.usermanagement.entities.User;
import tn.esprit.usermanagement.repositories.EventRepo;
import tn.esprit.usermanagement.repositories.PicturesRepo;
import tn.esprit.usermanagement.repositories.TicketRepo;
import tn.esprit.usermanagement.repositories.UserRepo;
import tn.esprit.usermanagement.services.TicketService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    @Override
    public Ticket AddTicketForEventAndAssignToUser(Ticket ticket, Integer userId, Integer eventId) throws IOException, WriterException {
        User user = userRepo.findById(userId).orElse(null);
        Event event = eventRepo.findById(eventId).orElse(null);
        Ticket savedTicket = ticketRepo.save(ticket);
        savedTicket.setUser(user);
        savedTicket.setEvent(event);

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

        // Create a new Pictures object with the PNG data
        Pictures picture = new Pictures();
        picture.setData(pngData);
        picturesRepo.save(picture);

        // Add the Pictures object to the list of QR codes in the Ticket object
        List<Pictures> Qrcodes = new ArrayList<>();
        Qrcodes.add(picture);
        savedTicket.setQrCodeList(Qrcodes);

        // Create a new PDF document
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = new PdfWriter(pdfOutputStream);
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        Document document = new Document(pdfDocument);

        // Add a title to the PDF
        Paragraph title = new Paragraph("Ticket for Event " + event.getTitle());
        title.setTextAlignment(TextAlignment.CENTER);
        document.add(title);

        // Add a description to the PDF
        Paragraph description = new Paragraph("This ticket is for " + event.getTitle() + ", which is taking place on " + event.getStartDate() + " at " + event.getStartDate() + ".");
        description.setTextAlignment(TextAlignment.JUSTIFIED);
        document.add(description);

        // Add the QR code image to the PDF
        Image qrCodePdfImage = new Image(ImageDataFactory.create(pngData));
        qrCodePdfImage.scaleToFit(200, 200);
        document.add(qrCodePdfImage);

        // Add other information to the PDF
        Paragraph userInfo = new Paragraph("User Information:");
        userInfo.setBold();
        userInfo.setMarginTop(20);
        document.add(userInfo);

        Paragraph name = new Paragraph("Name: " + user.getFirstName() + " " + user.getLastName());
        document.add(name);

        Paragraph email = new Paragraph("Email: " + user.getEmail());
        document.add(email);

        // Close the PDF document
        document.close();

        byte[] pdfData = pdfOutputStream.toByteArray();

        // Save the PDF to a file
        String fileName = savedTicket.getId().toString() + ".pdf";
        String filePath = "src/main/resources/assets/" + fileName;
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(pdfData);
        fos.flush();
        fos.close();
    // Save the QR code image to a file
        String qrCodeFilename = savedTicket.getId().toString() + ".png";
        File qrCodeFile = new File("src/main/resources/assets/" + qrCodeFilename);
        FileOutputStream qrCodeFos = new FileOutputStream(qrCodeFile);
        qrCodeFos.write(pngData);
        qrCodeFos.flush();
        qrCodeFos.close();
        // Create a new Pictures object with the PDF data
        Pictures pdfPicture = new Pictures();
        pdfPicture.setData(pdfData);
        picturesRepo.save(pdfPicture);

        // Add the Pictures object to the list of QR codes in the Ticket object
        Qrcodes.add(pdfPicture);
        return savedTicket;
    }
    }
