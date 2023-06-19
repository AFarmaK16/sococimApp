package com.example.demo.services;

import com.sun.mail.smtp.SMTPTransport;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Properties;

import static com.example.demo.constant.EmailConstant.*;


@Service
public class EmailService {
    public ResponseEntity<String> sendEmail(String firstName, String email) throws MessagingException {
        System.out.println("SENDING EMAIL TO "+firstName+" CAUSE LOCKED");
        email="farmakane@esp.sn";
        try {
            Message message = createAccountLockedEmail(firstName, email);
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
    private Message createAccountLockedEmail(String firstName, String email) throws MessagingException {

        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
//        message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(CC_EMAIL,false));
        message.setSubject(EMAIL_SUBJECT);
        message.setText("Bonjour " + firstName + ",\n" +
                "\n" +
                "Nous regrettons de vous informer que votre compte a été bloqué en raison de nombreuses tentatives infructueuses. Votre sécurité est notre priorité, et cette mesure a été prise pour protéger votre compte.\n" +
                "\n" +
                "Pour résoudre ce problème et réactiver votre compte, veuillez contacter notre service commercial au numéro suivant : [7720...]. Nos représentants se feront un plaisir de vous assister  dans le processus de déblocage de votre compte.\n" +
                "\n" +
                "Nous nous excusons pour les désagréments que cela a pu causer et vous remercions de votre compréhension.\n" +
                "\n" +
                "Cordialement,\n" +
                "L'équipe du support client\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n");
        message.setSentDate(new Date());
        message.saveChanges();
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
