package lk.GamerShop;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class GmailTest {
    public static void main(String[] args) throws Exception {
        String username = "plshaponso@gmail.com";
        String password = "uwkbhjycguxlfbt";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse("plshaponso@gmail.com")); // send to yourself
        message.setSubject("Test Gmail App Password");
        message.setText("If you see this, it works!");

        Transport.send(message);
        System.out.println("Email sent successfully!");
    }
}
