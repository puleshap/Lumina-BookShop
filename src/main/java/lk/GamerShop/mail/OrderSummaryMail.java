package lk.GamerShop.mail;

import io.rocketbase.mail.model.HtmlTextEmail;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lk.GamerShop.Utils.Env;
import lk.GamerShop.DTO.OrderDTO;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OrderSummaryMail extends Mailable {

    private final String to;
    private final OrderDTO orderDTO;

    public OrderSummaryMail(String to, OrderDTO orderDTO) {
        this.to = to;
        this.orderDTO = orderDTO;
    }

    @Override
    public void build(Message message) throws MessagingException {
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("Your Order Summary - " + Env.get("app.name"));

        String appURL = Env.get("app.url");

        // Use standard "cid:logo" to reference the local asset attached later
        HtmlTextEmail htmlTextEmail = getEmailTemplateBuilder()
                .header()
                .logo("lk/GamerShop/mail/logo.png").logoHeight(40).and()
                .text("ORDER CONFIRMATION")
                .h1()
                .center()
                .and()
                .text("Thank you for your order! Here is your order summary:")
                .center()
                .and()
                .text("Order Number: #" + orderDTO.getOrderNumber())
                .h3()
                .and()
                .text("Order Date: " + orderDTO.getOrderDate())
                .and()
                .text("Total Amount: LKR. " + orderDTO.getTotalAmount()) // Adjust currency symbol as needed
                .h2()
                .and()
                .copyright(Env.get("app.name"))
                .url(appURL)
                .suffix(". All Rights Reserved")
                .and()
                .build();


        message.setContent(htmlTextEmail.getHtml(), "text/html; charset=utf-8");
    }
}