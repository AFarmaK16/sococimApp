package com.example.demo.services;

import com.example.demo.beans.Orders;
import com.sun.mail.smtp.SMTPTransport;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Properties;

import static com.example.demo.constant.EmailConstant.*;


@Service @RequiredArgsConstructor
public class EmailService {
    private final OrderService orderService;
    public ResponseEntity<String> sendEmail(String firstName, String email,String emailText,String emailSubject) throws MessagingException {
//        email="farmakane@esp.sn";
        try {
            Message message = createEmail(firstName, email, emailText, emailSubject);
            SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(MAIL_PROTOCOL);
            smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
            smtpTransport.sendMessage(message, message.getAllRecipients());
            smtpTransport.close();
            return ResponseEntity.ok("Mail envoyé avec succés!!");
//            System.out.println("Mail envoyé avec succés!!");
        }
         catch (Exception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'envoie du mail!");
        }
    }
    private Message createEmail(String firstName, String email,String emailText,String emailSubject) throws MessagingException {
        System.out.println("SENDING EMAIL TO "+firstName+" .....");
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
//        message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(CC_EMAIL,false));
        message.setSubject(emailSubject);
        message.setText("Bonjour " + firstName + ",\n" +
                emailText);
        message.setSentDate(new Date());
        message.saveChanges();
        return message;


    }
    public String createOrderEmailText(Integer orderId) {
        Orders order = orderService.getOrdersById(orderId);
       String message=  "\n\nCommande N°"+order.getOrder_id()+ "\n" +"Date: "+order.getOrder_Date()+
                "Cordialement,\n" +
                "L'équipe du support client\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n";
        return message;


    }

    private Session getEmailSession(){
        Properties properties = System.getProperties();
        properties.put(SMTP_HOST,GMAIL_SMTP_SERVER);
        properties.put(SMTP_AUTH,true);
        properties.put(SMTP_PORT,DEFAULT_PORT);
        properties.put(SMTP_STARTTLS_ENABLE,true);
        properties.put(SMTP_STARTTLS_REQUIRED,true);
        return Session.getInstance(properties,null);
    }
}
